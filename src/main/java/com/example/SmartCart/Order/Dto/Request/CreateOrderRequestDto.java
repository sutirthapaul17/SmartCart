package com.example.SmartCart.Order.Dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequestDto {

    @NotNull(message = "Shipping address is required.")
    private Long shippingAddressId;

    @NotEmpty(message = "Order must contain at least one item.")
    private List<OrderItemRequestDto> items;

}
