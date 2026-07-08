package com.example.SmartCart.Products.Dto.RequestDto;


import java.math.BigDecimal;

public record UpdateProductRequest(

        String name,

        String description,

        Long categoryId,

        BigDecimal price,

        Integer stock,

        Boolean active

) {
}
