package com.example.SmartCart.Order.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequestDto {

    @NotNull(message = "Product id is required.")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

}
