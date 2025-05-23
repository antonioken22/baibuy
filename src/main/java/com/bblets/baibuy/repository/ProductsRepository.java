package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {

        Page<Product> findByCreatedBy(Integer userId, Pageable pageable);

        Optional<Product> findByIdAndCreatedBy(Integer id, Integer userId);

        Page<Product> findByBlockedTrue(Pageable pageable);

        Page<Product> findAllByIsListedTrueAndBlockedFalse(Pageable pageable);

        List<Product> findByIsListedTrue(Sort sort);

        List<Product> findByCreatedBy(Integer createdBy);

}