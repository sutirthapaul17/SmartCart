package com.example.SmartCart.Order.Dto.Response;

import com.example.SmartCart.Order.Enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponseDto(
        Long orderId,

        String orderNumber,

        OrderStatus status,

        Integer totalItems,

        BigDecimal totalAmount,

        LocalDateTime orderedAt

){}