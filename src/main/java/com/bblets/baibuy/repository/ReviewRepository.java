package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.Review;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByReviewedUser(User reviewedUser);

    List<Review> findByProduct(Product product);

    boolean existsByReviewerAndProduct(User reviewer, Product product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);

}
