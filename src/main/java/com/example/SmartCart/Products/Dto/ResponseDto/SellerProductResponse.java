package com.example.SmartCart.Products.Dto.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SellerProductResponse(

        Long id,

        String name,

        BigDecimal price,

        Integer stock,

        Boolean active,

        LocalDateTime createdAt

) {
}