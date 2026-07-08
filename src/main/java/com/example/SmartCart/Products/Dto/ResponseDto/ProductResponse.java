package com.example.SmartCart.Products.Dto.ResponseDto;

import java.math.BigDecimal;

public record ProductResponse(

        Long id,

        String name,

        BigDecimal price,

        String thumbnail,

        String category,

        Boolean active

) {
}
