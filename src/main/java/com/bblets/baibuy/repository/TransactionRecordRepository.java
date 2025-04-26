package com.bblets.baibuy.repository;

import com.bblets.baibuy.models.TransactionRecord;
import com.bblets.baibuy.models.User;
import com.bblets.baibuy.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Integer> {

    Optional<TransactionRecord> findByBuyerAndProduct(User buyer, Product product);

    List<TransactionRecord> findByBuyer(User buyer);

    List<TransactionRecord> findBySeller(User seller);

    List<TransactionRecord> findBySellerId(Integer sellerId);

}
