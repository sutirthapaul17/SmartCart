package com.example.SmartCart.ReviewAndRatings.Service.Impl;

import com.example.SmartCart.ReviewAndRatings.Dto.CreateReviewRequest;
import com.example.SmartCart.ReviewAndRatings.Dto.ReviewResponse;
import com.example.SmartCart.ReviewAndRatings.Dto.ReviewSummaryResponse;
import com.example.SmartCart.ReviewAndRatings.Dto.UpdateReviewRequest;
import com.example.SmartCart.ReviewAndRatings.Service.ReviewService;

import java.util.List;

public class ReviewServiceImpl implements ReviewService{
    @Override
    public ReviewResponse createReview(Long productId, Long userId, CreateReviewRequest request) {
        return null;
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, Long userId, UpdateReviewRequest request) {
        return null;
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {

    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return List.of();
    }

    @Override
    public ReviewSummaryResponse getReviewSummary(Long productId) {
        return null;
    }
}
