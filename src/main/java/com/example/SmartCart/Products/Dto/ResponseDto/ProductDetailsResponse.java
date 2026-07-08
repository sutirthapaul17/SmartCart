package com.example.SmartCart.Products.Dto.ResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailsResponse(

        Long id,

        String name,

        String description,

        BigDecimal price,

        Integer stock,

        String seller,

        String category,

        List<String> images

) {
}
