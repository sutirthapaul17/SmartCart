package com.example.SmartCart.Checkout.Dto.Response;

import java.math.BigDecimal;

public record CheckoutItemResponseDto(
        Long cartItemId,
        Long productId,
        String productName,
        String productImage,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
