package com.example.SmartCart.Order.Dto.Response;

import com.example.SmartCart.Order.Enums.OrderStatus;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long orderId,

        String orderNumber,

        OrderStatus status,

        CustomerOrderResponseDto customer,

        AddressResponseDto shippingAddress,

        String phoneNumber,

        List<OrderItemResponseDto> items,

        BigDecimal subtotal,

        BigDecimal gstRate,

        BigDecimal gstAmount,

        BigDecimal deliveryCharge,

        BigDecimal discount,

        BigDecimal totalAmount,

        LocalDateTime orderedAt

){}