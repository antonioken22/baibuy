package com.bblets.baibuy.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bblets.baibuy.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
