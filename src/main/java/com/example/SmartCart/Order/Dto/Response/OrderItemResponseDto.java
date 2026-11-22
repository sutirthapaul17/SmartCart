package com.example.SmartCart.Order.Dto.Response;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long productId,

        String productName,

        String productImage,

        Integer quantity,

        BigDecimal price,

        BigDecimal totalPrice
) {}
