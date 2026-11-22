package com.example.SmartCart.Checkout.Dto.Response;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutResponseDto(
        List<CheckoutItemResponseDto> items,
        BigDecimal subtotal,
        BigDecimal gstRate,
        BigDecimal gstAmount,
        BigDecimal deliveryCharge,
        BigDecimal discount,
        BigDecimal totalAmount
) {
}
