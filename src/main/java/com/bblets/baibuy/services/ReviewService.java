package com.bblets.baibuy.services;

import com.bblets.baibuy.models.User;
import com.bblets.baibuy.models.Product;
import com.bblets.baibuy.models.TransactionRecord;
import com.bblets.baibuy.models.ReviewDto;
import com.bblets.baibuy.models.Review;
import com.bblets.baibuy.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductsRepository productRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public List<Review> getReviewsByProduct(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewsByUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return reviewRepository.findByReviewedUser(user);
    }

    public boolean hasUserReviewed(Integer reviewerId, Integer productId) {
        User reviewer = userRepository.findById(reviewerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        return reviewRepository.existsByReviewerAndProduct(reviewer, product);
    }

    @Transactional
    public void submitReview(Integer reviewerId, ReviewDto dto) {
        User reviewer = userRepository.findById(reviewerId).orElseThrow();
        User reviewedUser = userRepository.findById(dto.getReviewedUserId()).orElseThrow();
        Product product = productRepository.findById(dto.getProductId()).orElseThrow();

        TransactionRecord record = transactionRecordRepository.findByBuyerAndProduct(reviewer, product)
                .orElseThrow(() -> new IllegalStateException("No confirmed transaction found"));

        if (record.isBuyerReviewed()) {
            throw new IllegalStateException("You have already reviewed this transaction.");
        }

        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewedUser(reviewedUser)
                .product(product)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .transactionDate(record.getConfirmedAt())
                .build();

        reviewRepository.save(review);

        record.setBuyerReviewed(true);
        transactionRecordRepository.save(record);
    }

    @Transactional
    public void submitSellerReview(Integer sellerId, ReviewDto dto) {
        User seller = userRepository.findById(sellerId).orElseThrow();
        User buyer = userRepository.findById(dto.getReviewedUserId()).orElseThrow();
        Product product = productRepository.findById(dto.getProductId()).orElseThrow();

        TransactionRecord record = transactionRecordRepository.findByBuyerAndProduct(buyer, product)
                .orElseThrow(() -> new IllegalStateException("No transaction record found"));

        if (record.isSellerReviewed()) {
            throw new IllegalStateException("You have already rated this buyer.");
        }

        Review review = Review.builder()
                .reviewer(seller)
                .reviewedUser(buyer)
                .product(product)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .transactionDate(record.getConfirmedAt())
                .build();

        reviewRepository.save(review);
        record.setSellerReviewed(true);
        transactionRecordRepository.save(record);
    }

}
