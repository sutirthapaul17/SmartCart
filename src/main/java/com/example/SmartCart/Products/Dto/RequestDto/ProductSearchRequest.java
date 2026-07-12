package com.example.SmartCart.Products.Dto.RequestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductSearchRequest(

        @Positive(message = "Category id must be positive.")
        Long categoryId,

        @DecimalMin(value = "0.0", inclusive = true,
                message = "Minimum price cannot be negative.")
        BigDecimal minPrice,

        @DecimalMin(value = "0.0", inclusive = true,
                message = "Maximum price cannot be negative.")
        BigDecimal maxPrice,

        @Pattern(
                regexp = "^(name|price|createdAt)$",
                message = "Sort by must be one of: name, price, createdAt."
        )
        String sortBy,

        @Pattern(
                regexp = "^(asc|desc|ASC|DESC)$",
                message = "Direction must be asc or desc."
        )
        String direction

) {}