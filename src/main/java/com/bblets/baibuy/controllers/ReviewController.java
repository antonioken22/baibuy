package com.bblets.baibuy.controllers;

import com.bblets.baibuy.models.ReviewDto;
import com.bblets.baibuy.models.Review;
import com.bblets.baibuy.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.bblets.baibuy.security.AuthUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public List<Review> getProductReviews(@PathVariable Integer productId) {
        return reviewService.getReviewsByProduct(productId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getUserReviews(@PathVariable Integer userId) {
        return reviewService.getReviewsByUser(userId);
    }

    @PostMapping("/create")
    public void submitReview(
            @AuthenticationPrincipal AuthUserDetails authUser,
            @Valid @ModelAttribute ReviewDto reviewDto) {
        reviewService.submitReview(authUser.getId(), reviewDto);
    }

    @PostMapping("/seller-review")
    public void submitSellerReview(
            @AuthenticationPrincipal AuthUserDetails authUser,
            @Valid @ModelAttribute ReviewDto reviewDto) {
        reviewService.submitSellerReview(authUser.getId(), reviewDto);
    }

}
