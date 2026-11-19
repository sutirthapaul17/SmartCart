package com.example.SmartCart.Order.Dto.Response;

import com.example.SmartCart.Order.Enums.OrderStatus;
import com.example.SmartCart.User.Dto.Address.AddressResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long orderId;

    private String orderNumber;

    private OrderStatus status;

    private CustomerOrderResponseDto customer;

    private AddressResponseDto shippingAddress;

    private String phoneNumber;

    private List<OrderItemResponseDto> items;

    private BigDecimal subtotal;

    private BigDecimal gstAmount;

    private BigDecimal deliveryCharge;

    private BigDecimal discount;

    private BigDecimal totalAmount;

    private LocalDateTime orderedAt;

}