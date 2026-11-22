package com.example.SmartCart.Cart.Dto.Response;

import java.math.BigDecimal;

public record CartItemRes(
        Long cartItemId,

        Long productId,

        String productName,

        String thumbnail,

        BigDecimal unitPrice,

        Integer quantity,

        Integer availableStock,

        BigDecimal subtotal
) {
}
