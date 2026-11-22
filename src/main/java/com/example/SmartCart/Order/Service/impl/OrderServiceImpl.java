
package com.example.SmartCart.Order.Service.impl;

import com.example.SmartCart.Cart.Entity.Cart;
import com.example.SmartCart.Cart.Entity.CartItem;
import com.example.SmartCart.Cart.Repo.CartItemRepo;
import com.example.SmartCart.Cart.Repo.CartRepo;
import com.example.SmartCart.Checkout.Dto.Response.CheckoutResponseDto;
import com.example.SmartCart.Checkout.Service.CheckoutService;
import com.example.SmartCart.Order.Dto.Request.CreateOrderRequestDto;
import com.example.SmartCart.Order.Dto.Response.OrderResponseDto;
import com.example.SmartCart.Order.Dto.Response.OrderSummaryResponseDto;
import com.example.SmartCart.Order.Entity.Order;
import com.example.SmartCart.Order.Entity.OrderAddressSnapshot;
import com.example.SmartCart.Order.Entity.OrderItem;
import com.example.SmartCart.Order.Enums.OrderStatus;
import com.example.SmartCart.Order.Repository.OrderRepository;
import com.example.SmartCart.Order.Service.OrderService;
import com.example.SmartCart.Mapper.Order.OrderMapper;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Entity.ProductImage;
import com.example.SmartCart.Products.Repository.ProductRepository;
import com.example.SmartCart.Products.enums.ProductStatus;
import com.example.SmartCart.User.Entity.Address;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.AddressRepo;
import com.example.SmartCart.common.Exception.BadRequestException;
import com.example.SmartCart.common.Exception.ConflictException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ProductRepository productRepository;
    private final AddressRepo addressRepo;
    private final CheckoutService checkoutService;
    private final OrderMapper orderMapper;
    private final InvoicePdfService invoicePdfService;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {

        User customer = currentUser();

        // Recalculate prices at the moment of order creation.
        CheckoutResponseDto checkout =
                checkoutService.calculateCheckout(
                        request.shippingAddressId(),
                        request.cartItemIds()
                );

        Address address = addressRepo.findByIdAndUser_Id(
                request.shippingAddressId(),
                customer.getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Shipping address not found."
                )
        );

        Cart cart = cartRepository.findByUser_Id(customer.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Your cart is empty."
                        )
                );

        List<CartItem> selectedItems =
                cartItemRepository.findAllByCart_IdAndIdIn(
                        cart.getId(),
                        request.cartItemIds()
                );

        if (selectedItems.size() != request.cartItemIds().size()) {
            throw new BadRequestException(
                    "Selected cart items are no longer available."
            );
        }

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customer(customer)
                .shippingAddress(address)
                .shippingAddressSnapshot(
                        createAddressSnapshot(address)
                )
                .status(OrderStatus.PENDING)
                .subtotal(checkout.subtotal())
                .gstAmount(checkout.gstAmount())
                .deliveryCharge(checkout.deliveryCharge())
                .discount(checkout.discount())
                .totalAmount(checkout.totalAmount())
                .orderItems(new ArrayList<>())
                .build();

        for (CartItem cartItem : selectedItems) {

            // Lock and re-read each product before changing stock.
            Product product = productRepository
                    .findByIdForUpdate(cartItem.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found."
                            )
                    );

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new ConflictException(
                        "Product is no longer available: " +
                                product.getName()
                );
            }

            if (product.getStock() == null ||
                    product.getStock() < cartItem.getQuantity()) {
                throw new ConflictException(
                        "Insufficient stock for: " + product.getName()
                );
            }

            // Use the locked product's current price, not a client value.
            BigDecimal unitPrice = product.getPrice();

            BigDecimal lineTotal = unitPrice.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            String image = getPrimaryImage(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .seller(product.getSeller())
                    .productName(product.getName())
                    .productImage(image)
                    .quantity(cartItem.getQuantity())
                    .price(unitPrice)
                    .totalPrice(lineTotal)
                    .build();

            order.getOrderItems().add(orderItem);

            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            if (product.getStock() == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }
        }

        Order savedOrder = orderRepository.save(order);

        // Remove only the cart items included in this order.
        selectedItems.forEach(cart::removeItem);
        cartRepository.save(cart);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponseDto> getMyOrders() {

        User customer = currentUser();

        return orderRepository
                .findByCustomer_IdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(orderMapper::toOrderSummary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {

        User customer = currentUser();

        Order order = orderRepository
                .findByIdAndCustomer_Id(orderId, customer.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        )
                );

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponseDto cancelOwnOrder(Long orderId) {

        User customer = currentUser();

        Order order = orderRepository
                .findByIdAndCustomer_IdForUpdate(
                        orderId,
                        customer.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED) {
            throw new ConflictException(
                    "This order can no longer be cancelled."
            );
        }

        for (OrderItem item : order.getOrderItems()) {

            Product product = productRepository
                    .findByIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found."
                            )
                    );

            product.setStock(
                    product.getStock() + item.getQuantity()
            );

            if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
                product.setStatus(ProductStatus.ACTIVE);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadInvoice(Long orderId) {

        User customer = currentUser();

        Order order = orderRepository
                .findByIdAndCustomer_Id(orderId, customer.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        )
                );

        return invoicePdfService.generateInvoice(order);
    }

    private User currentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof User user)) {
            throw new BadRequestException(
                    "Authenticated user could not be resolved."
            );
        }

        return user;
    }

    private String generateOrderNumber() {
        return "SC-" + UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }

    private OrderAddressSnapshot createAddressSnapshot(Address address) {
        return OrderAddressSnapshot.builder()
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    private String getPrimaryImage(Product product) {
        if (product.getImages() == null) {
            return null;
        }

        return product.getImages().stream()
                .sorted(
                        java.util.Comparator.comparing(
                                (ProductImage image) ->
                                        !Boolean.TRUE.equals(
                                                image.getPrimaryImage()
                                        )
                        ).thenComparing(
                                ProductImage::getDisplayOrder,
                                java.util.Comparator.nullsLast(
                                        Integer::compareTo
                                )
                        )
                )
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }
}
