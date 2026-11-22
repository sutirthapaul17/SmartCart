package com.example.SmartCart.Order.Dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequestDto (

        @NotNull(message = "Shipping address is required.")
        @Positive(message = "Invalid shipping address ID.")
        Long shippingAddressId,

        @NotEmpty(message = "Select at least one cart item.")
        List<@NotNull @Positive Long> cartItemIds
){}
