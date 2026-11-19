package com.example.SmartCart.Order.Dto.Response;

import com.example.SmartCart.Order.Enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummaryResponseDto {

    private Long orderId;

    private String orderNumber;

    private OrderStatus status;

    private Integer totalItems;

    private BigDecimal totalAmount;

    private LocalDateTime orderedAt;

}