
package com.example.SmartCart.Checkout.Service.Impl;

import com.example.SmartCart.Cart.Entity.Cart;
import com.example.SmartCart.Cart.Entity.CartItem;
import com.example.SmartCart.Cart.Repo.CartItemRepo;
import com.example.SmartCart.Cart.Repo.CartRepo;
import com.example.SmartCart.Checkout.Dto.Response.CheckoutItemResponseDto;
import com.example.SmartCart.Checkout.Dto.Response.CheckoutResponseDto;
import com.example.SmartCart.Checkout.Service.CheckoutService;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Entity.ProductImage;
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
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckoutServiceImpl implements CheckoutService {

    private static final BigDecimal GST_RATE =
            new BigDecimal("0.05");

    private static final BigDecimal DELIVERY_CHARGE =
            new BigDecimal("10.00");

    private static final BigDecimal DISCOUNT =
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final AddressRepo addressRepo;

    @Override
    public CheckoutResponseDto calculateCheckout(
            Long shippingAddressId,
            List<Long> cartItemIds) {

        User user = currentUser();

        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new BadRequestException(
                    "Select at least one cart item."
            );
        }

        Set<Long> uniqueIds = new HashSet<>(cartItemIds);

        if (uniqueIds.size() != cartItemIds.size()) {
            throw new BadRequestException(
                    "Duplicate cart item IDs are not allowed."
            );
        }

        addressRepo.findByIdAndUser_Id(
                shippingAddressId,
                user.getId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Shipping address not found."
                )
        );

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Your cart is empty."
                        )
                );

        List<CartItem> cartItems =
                cartItemRepository.findAllByCart_IdAndIdIn(
                        cart.getId(),
                        cartItemIds
                );

        if (cartItems.size() != cartItemIds.size()) {
            throw new BadRequestException(
                    "One or more selected items do not belong to your cart."
            );
        }

        List<CheckoutItemResponseDto> responseItems =
                cartItems.stream()
                        .map(this::toCheckoutItem)
                        .toList();

        BigDecimal subtotal = responseItems.stream()
                .map(CheckoutItemResponseDto::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal gstAmount = subtotal
                .multiply(GST_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = subtotal
                .add(gstAmount)
                .add(DELIVERY_CHARGE)
                .subtract(DISCOUNT)
                .setScale(2, RoundingMode.HALF_UP);

        return new CheckoutResponseDto(
                responseItems,
                subtotal,
                GST_RATE,
                gstAmount,
                DELIVERY_CHARGE,
                DISCOUNT,
                totalAmount
        );
    }

    private CheckoutItemResponseDto toCheckoutItem(
            CartItem item) {

        Product product = item.getProduct();

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ConflictException(
                    "Product is not available: " + product.getName()
            );
        }

        if (product.getStock() == null ||
                product.getStock() < item.getQuantity()) {
            throw new ConflictException(
                    "Insufficient stock for: " + product.getName()
            );
        }

        BigDecimal lineTotal = product.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        return new CheckoutItemResponseDto(
                item.getId(),
                product.getId(),
                product.getName(),
                getPrimaryImage(product),
                item.getQuantity(),
                product.getPrice(),
                lineTotal
        );
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
}
