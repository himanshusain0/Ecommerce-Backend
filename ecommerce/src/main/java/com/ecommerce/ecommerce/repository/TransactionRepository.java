package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.modal.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction ,Long> {
    List<Transaction> findBySellerId(Long sellerId);
}
