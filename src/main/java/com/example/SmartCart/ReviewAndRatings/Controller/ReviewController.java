//package com.example.SmartCart.ReviewAndRatings.Controller;
//
//import com.example.SmartCart.ReviewAndRatings.Dto.CreateReviewRequest;
//import com.example.SmartCart.ReviewAndRatings.Dto.ReviewResponse;
//import com.example.SmartCart.ReviewAndRatings.Dto.UpdateReviewRequest;
//import com.example.SmartCart.ReviewAndRatings.Service.ReviewService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    /**
//     * Get all reviews for a product
//     * Accessible by everyone (including guests)
//     */
//    @GetMapping("${api.prefix}/products/{productId}/reviews")
//    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
//            @PathVariable Long productId
//    ) {
//        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
//    }
//
//    /**
//     * Create a review
//     * Only verified purchasers
//     */
//    @PostMapping("${api.prefix}/products/{productId}/reviews")
//    public ResponseEntity<ReviewResponse> createReview(
//            @PathVariable Long productId,
//            @Valid @RequestBody CreateReviewRequest request,
//            @AuthenticationPrincipal(expression = "id") Long userId
//    ) {
//
//        ReviewResponse response =
//                reviewService.createReview(productId, userId, request);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    /**
//     * Update own review
//     */
//    @PutMapping("${api.prefix}/reviews/{reviewId}")
//    public ResponseEntity<ReviewResponse> updateReview(
//            @PathVariable Long reviewId,
//            @Valid @RequestBody UpdateReviewRequest request,
//            @AuthenticationPrincipal(expression = "id") Long userId
//    ) {
//
//        return ResponseEntity.ok(
//                reviewService.updateReview(reviewId, userId, request)
//        );
//    }
//
//    /**
//     * Delete own review
//     */
//    @DeleteMapping("${api.prefix}/reviews/{reviewId}")
//    public ResponseEntity<String> deleteReview(
//            @PathVariable Long reviewId,
//            @AuthenticationPrincipal(expression = "id") Long userId
//    ) {
//
//        reviewService.deleteReview(reviewId, userId);
//
//        return ResponseEntity.ok("Review deleted successfully.");
//    }
//
//}