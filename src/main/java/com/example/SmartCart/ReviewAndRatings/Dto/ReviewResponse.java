package com.example.SmartCart.ReviewAndRatings.Dto;

import java.time.LocalDateTime;

public record ReviewResponse(

        Long reviewId,

        Long productId,

        Long userId,

        String customerName,

        Integer rating,

        String comment,

        LocalDateTime createdAt

) {}