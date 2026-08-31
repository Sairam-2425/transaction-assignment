

package com.example.transactionstarter.repository;

import com.example.transactionstarter.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(String customerId);

    boolean existsByTransactionId(String transactionId);
    
    List<Transaction> findByTransactionId(String transactionId);
}





