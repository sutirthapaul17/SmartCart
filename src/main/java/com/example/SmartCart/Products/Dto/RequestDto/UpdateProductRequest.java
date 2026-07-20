package com.example.SmartCart.Products.Dto.RequestDto;


import com.example.SmartCart.Products.enums.ProductStatus;

import java.math.BigDecimal;

public record UpdateProductRequest(

        String name,

        String description,

        Long categoryId,

        BigDecimal price,

        Integer stock,

        ProductStatus status

) {
}
