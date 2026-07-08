package com.example.SmartCart.Products.Dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "Product name is required.")
        String name,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Category is required.")
        Long categoryId,

        @NotNull(message = "Price is required.")
        @Positive(message = "Price must be greater than zero.")
        BigDecimal price,

        @NotNull(message = "Stock is required.")
        @PositiveOrZero(message = "Stock cannot be negative.")
        Integer stock

) {
}
