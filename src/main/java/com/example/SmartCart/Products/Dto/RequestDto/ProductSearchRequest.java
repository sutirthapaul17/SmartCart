package com.example.SmartCart.Products.Dto.RequestDto;

import java.math.BigDecimal;

public record ProductSearchRequest(

        Long categoryId,

        BigDecimal minPrice,

        BigDecimal maxPrice,

        String sortBy,

        String direction

) {
}
