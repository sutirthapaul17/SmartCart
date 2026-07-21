package com.example.SmartCart.Products.Dto.ResponseDto;

import com.example.SmartCart.Products.enums.ProductStatus;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,

        String name,

        BigDecimal price,

        String thumbnail,

        String category,

        ProductStatus active

) {
}
