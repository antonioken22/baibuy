package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByCreatedBy(Integer userId, Pageable pageable);

    Optional<Product> findByIdAndCreatedBy(Integer id, Integer userId);

    Page<Product> findAllByIsListedTrueAndIsBlockedFalse(Pageable pageable);

    Page<Product> findByIsBlockedTrue(Pageable pageable);
}