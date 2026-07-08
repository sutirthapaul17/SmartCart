package com.example.SmartCart.ReviewAndRatings.Dto;

public record ReviewSummaryResponse(
        Double averageRating,
        Long totalReviews
) {}