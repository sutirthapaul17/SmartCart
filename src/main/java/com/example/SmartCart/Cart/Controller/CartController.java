package com.example.SmartCart.Cart.Controller;

import com.example.SmartCart.Cart.Dto.Request.AddCartItemReq;
import com.example.SmartCart.Cart.Dto.Request.UpdateCartItemReq;
import com.example.SmartCart.Cart.Dto.Response.CartResponse;
import com.example.SmartCart.Cart.Service.CartService;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.common.Handler.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(
        name = "Cart Management",
        description = "APIs for managing the authenticated customer's shopping cart"
)
public class CartController {

    private final CartService cartService;

    // Get the currently authenticated user's ID
    private Long getCurrentUserId() {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return user.getId();
    }

    // 1. View Cart
    @GetMapping
    @Operation(
            summary = "Get My Cart",
            description = "Retrieves all products in the authenticated customer's cart along with quantities and subtotal."
    )
    public ResponseEntity<ApiResponse<CartResponse>> getMyCart() {

        CartResponse response =
                cartService.getMyCart(getCurrentUserId());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Cart fetched successfully.",
                        response
                )
        );
    }

    // 2. Add Product to Cart
    @PostMapping("/items")
    @Operation(
            summary = "Add Product to Cart",
            description = "Adds a product to the authenticated customer's cart. If the product already exists, its quantity is increased."
    )
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @Valid @RequestBody AddCartItemReq request) {

        CartResponse response =
                cartService.addItem(
                        getCurrentUserId(),
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Product added to cart successfully.",
                        response
                )
        );
    }

    // 3. Update Product Quantity
    @PatchMapping("/items/{productId}")
    @Operation(
            summary = "Update Cart Item Quantity",
            description = "Changes the quantity of an existing product in the cart."
    )
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemReq request) {

        CartResponse response =
                cartService.updateQuantity(
                        getCurrentUserId(),
                        productId,
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Cart item quantity updated successfully.",
                        response
                )
        );
    }

    // 4. Remove Product from Cart
    @DeleteMapping("/items/{productId}")
    @Operation(
            summary = "Remove Product from Cart",
            description = "Removes the specified product from the authenticated customer's cart."
    )
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(
            @PathVariable Long productId) {

        CartResponse response =
                cartService.removeItem(
                        getCurrentUserId(),
                        productId
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Product removed from cart successfully.",
                        response
                )
        );
    }

    // 5. Clear Entire Cart
    @DeleteMapping
    @Operation(
            summary = "Clear My Cart",
            description = "Removes all products from the authenticated customer's cart."
    )
    public ResponseEntity<ApiResponse<CartResponse>> clearCart() {

        CartResponse response =
                cartService.clearCart(getCurrentUserId());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Cart cleared successfully.",
                        response
                )
        );
    }
}
