package com.example.SmartCart.ReviewAndRatings.Service;

import com.example.SmartCart.ReviewAndRatings.Dto.CreateReviewRequest;
import com.example.SmartCart.ReviewAndRatings.Dto.ReviewResponse;
import com.example.SmartCart.ReviewAndRatings.Dto.ReviewSummaryResponse;
import com.example.SmartCart.ReviewAndRatings.Dto.UpdateReviewRequest;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(Long productId,
                                Long userId,
                                CreateReviewRequest request);

    ReviewResponse updateReview(Long reviewId,
                                Long userId,
                                UpdateReviewRequest request);

    void deleteReview(Long reviewId,
                      Long userId);

    List<ReviewResponse> getReviewsByProduct(Long productId);

    ReviewSummaryResponse getReviewSummary(Long productId);
}