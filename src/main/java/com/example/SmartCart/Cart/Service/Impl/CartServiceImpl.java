package com.example.SmartCart.Cart.Service.Impl;


import com.example.SmartCart.Cart.Dto.Request.AddCartItemReq;
import com.example.SmartCart.Cart.Dto.Request.UpdateCartItemReq;
import com.example.SmartCart.Cart.Dto.Response.CartItemRes;
import com.example.SmartCart.Cart.Dto.Response.CartResponse;
import com.example.SmartCart.Cart.Entity.Cart;
import com.example.SmartCart.Cart.Entity.CartItem;
import com.example.SmartCart.Cart.Repo.CartItemRepo;
import com.example.SmartCart.Cart.Repo.CartRepo;
import com.example.SmartCart.Cart.Service.CartService;
import com.example.SmartCart.Mapper.Cart.CartMapper;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Entity.ProductImage;
import com.example.SmartCart.Products.Repository.ProductRepository;
import com.example.SmartCart.Products.enums.ProductStatus;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.common.Exception.BadRequestException;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl  implements CartService {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepo userRepo;
    private final CartMapper cartMapper;


    // --------------------------------------------------
    // 1. GET MY CART
    // --------------------------------------------------

    @Override
    public CartResponse getMyCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElse(null);

        if (cart == null) {
            return buildEmptyCartResponse();
        }

        List<CartItem> items =
                cartItemRepository.findAllByCart_Id(cart.getId());

        return buildCartResponse(cart, items);
    }

    // --------------------------------------------------
    // 2. ADD PRODUCT TO CART
    // --------------------------------------------------

    @Override
    public CartResponse addItem(
            Long userId,
            AddCartItemReq request) {

        // Find the product.
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.productId()
                        )
                );

        // Check whether the product is active.
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException(
                    "This product is currently unavailable."
            );
        }

        // Check stock availability.
        if (product.getStock() == null ||
                product.getStock() < request.quantity()) {

            throw new BadRequestException(
                    "Insufficient stock. Available quantity: "
                            + product.getStock()
            );
        }

        // Find the existing cart or create one.
        Cart cart = getOrCreateCart(userId);

        // Check whether this product is already in the cart.
        CartItem existingItem =
                cartItemRepository
                        .findByCart_IdAndProduct_Id(
                                cart.getId(),
                                product.getId()
                        )
                        .orElse(null);

        if (existingItem != null) {

            // Add the requested quantity to the existing quantity.
            int newQuantity = Math.addExact(
                    existingItem.getQuantity(),
                    request.quantity()
            );

            // Check stock against the new total quantity.
            if (newQuantity > product.getStock()) {
                throw new BadRequestException(
                        "Insufficient stock. Available quantity: "
                                + product.getStock()
                );
            }

            existingItem.setQuantity(newQuantity);

            cartItemRepository.save(existingItem);

        } else {

            // Create a new cart item.
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();

            cart.addItem(cartItem);

            cartItemRepository.save(cartItem);
        }

        touchCart(cart);

        return getCartResponse(cart);
    }


    // --------------------------------------------------
    // 3. UPDATE PRODUCT QUANTITY
    // --------------------------------------------------

    @Override
    public CartResponse updateQuantity(
            Long userId,
            Long productId,
            UpdateCartItemReq request) {

        Cart cart = getExistingCart(userId);

        CartItem cartItem = cartItemRepository
                .findByCart_IdAndProduct_Id(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found in your cart."
                        )
                );

        Product product = cartItem.getProduct();

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException(
                    "This product is currently unavailable."
            );
        }

        // Check requested quantity against available stock.
        if (product.getStock() == null ||
                request.quantity() > product.getStock()) {

            throw new BadRequestException(
                    "Insufficient stock. Available quantity: "
                            + product.getStock()
            );
        }

        // Set the exact new quantity.
        cartItem.setQuantity(request.quantity());

        cartItemRepository.save(cartItem);

        touchCart(cart);

        return getCartResponse(cart);
    }


    // --------------------------------------------------
    // 4. REMOVE PRODUCT FROM CART
    // --------------------------------------------------

    @Override
    public CartResponse removeItem(
            Long userId,
            Long productId) {

        Cart cart = getExistingCart(userId);

        CartItem cartItem = cartItemRepository
                .findByCart_IdAndProduct_Id(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found in your cart."
                        )
                );

        cart.removeItem(cartItem);

//        cartItemRepository.delete(cartItem);

        touchCart(cart);

        return getCartResponse(cart);
    }


    // --------------------------------------------------
    // 5. CLEAR ENTIRE CART
    // --------------------------------------------------

    @Override
    public CartResponse clearCart(Long userId) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElse(null);

        if (cart == null) {
            return buildEmptyCartResponse();
        }

        // Remove all child items.
        cart.clearItems();

        // Update the cart timestamp.
        touchCart(cart);

        return buildCartResponse(
                cart,
                List.of()
        );
    }




    // --------------------------------------------------
    // HELPER: GET OR CREATE CART
    // --------------------------------------------------

    private Cart getOrCreateCart(Long userId) {

        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    User user = userRepo.findById(userId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "User not found with id: "
                                                    + userId
                                    )
                            );

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });
    }













    // --------------------------------------------------
    // HELPER: BUILD CART RESPONSE
    // --------------------------------------------------

    private CartResponse buildCartResponse(
            Cart cart,
            List<CartItem> items) {

        List<CartItemRes> itemResponses = items.stream()
                .map(this::buildCartItemResponse)
                .toList();

        BigDecimal subtotal = itemResponses.stream()
                .map(CartItemRes::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalQuantity = itemResponses.stream()
                .mapToInt(CartItemRes::quantity)
                .sum();

        return new CartResponse(
                cart.getId(),
                itemResponses,
                itemResponses.size(),
                totalQuantity,
                subtotal,
                cart.getUpdatedAt()
        );
    }




    // --------------------------------------------------
    // HELPER: GET CART RESPONSE
    // --------------------------------------------------

    private CartResponse getCartResponse(Cart cart) {

        List<CartItem> items =
                cartItemRepository.findAllByCart_Id(cart.getId());

        return buildCartResponse(cart, items);
    }

    // --------------------------------------------------
    // HELPER: BUILD CART ITEM RESPONSE
    // --------------------------------------------------

//    private CartItemRes buildCartItemResponse(
//            CartItem cartItem) {
//
//        Product product = cartItem.getProduct();
//
//        BigDecimal unitPrice = product.getPrice();
//
//        BigDecimal itemSubtotal = unitPrice.multiply(
//                BigDecimal.valueOf(cartItem.getQuantity())
//        );
//
//        String thumbnail = product.getImages() == null
//                ? null
//                : product.getImages().stream()
//                .filter(ProductImage::getPrimaryImage)
//                .findFirst()
//                .or(() -> product.getImages().stream()
//                        .min(Comparator.comparing(
//                                ProductImage::getDisplayOrder,
//                                Comparator.nullsLast(
//                                        Comparator.naturalOrder()
//                                )
//                        )))
//                .map(ProductImage::getImageUrl)
//                .orElse(null);
//
//        return new CartItemRes(
//                cartItem.getId(),
//                product.getId(),
//                product.getName(),
//                thumbnail,
//                unitPrice,
//                cartItem.getQuantity(),
//                product.getStock(),
//                itemSubtotal
//        );
//    }

    private CartItemRes buildCartItemResponse(
            CartItem cartItem) {

        CartItemRes response =
                cartMapper.toCartItemResponse(cartItem);

        BigDecimal itemSubtotal =
                cartItem.getProduct()
                        .getPrice()
                        .multiply(
                                BigDecimal.valueOf(cartItem.getQuantity())
                        );

        return new CartItemRes(
                response.cartItemId(),
                response.productId(),
                response.productName(),
                response.thumbnail(),
                response.unitPrice(),
                response.quantity(),
                response.availableStock(),
                itemSubtotal
        );
    }


    // --------------------------------------------------
    // HELPER: EMPTY CART RESPONSE
    // --------------------------------------------------

    private CartResponse buildEmptyCartResponse() {

        return new CartResponse(
                null,
                List.of(),
                0,
                0,
                BigDecimal.ZERO,
                null
        );
    }

    // --------------------------------------------------
    // HELPER: UPDATE CART TIMESTAMP
    // --------------------------------------------------

    private void touchCart(Cart cart) {
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    // --------------------------------------------------
    // HELPER: GET EXISTING CART
    // --------------------------------------------------

    private Cart getExistingCart(Long userId) {

        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found for this user."
                        )
                );
    }



}
