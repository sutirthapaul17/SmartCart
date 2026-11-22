package com.example.SmartCart.Cart.Dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(
        Long cartId,

        List<CartItemRes> items,

        Integer distinctItemCount,

        Integer totalQuantity,

        BigDecimal subtotal,

        LocalDateTime updatedAt
) {
}
