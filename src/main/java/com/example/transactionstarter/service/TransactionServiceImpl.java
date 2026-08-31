package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import com.example.transactionstarter.model.Transaction;
import com.example.transactionstarter.model.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(
            CreateTransactionRequest request) {

        // Business validation:
        // Transaction ID must be unique.
        if (transactionRepository.existsByTransactionId(
                request.getTransactionId())) {

            throw new IllegalArgumentException(
                    "Transaction already exists: "
                            + request.getTransactionId());
        }

        Transaction transaction = new Transaction();

        transaction.setTransactionId(
                request.getTransactionId());

        transaction.setCustomerId(
                request.getCustomerId());

        transaction.setAmount(
                request.getAmount());

        transaction.setCurrency(
                request.getCurrency().name());

        transaction.setTransactionType(
                request.getTransactionType());

        // Every new transaction starts with PENDING status.
        transaction.setStatus(
                TransactionStatus.PENDING);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransaction(
            String transactionId) {

        List<Transaction> transactions =
                transactionRepository
                        .findByTransactionId(transactionId);

        if (transactions.isEmpty()) {
            throw new java.util.NoSuchElementException(
                    "Transaction not found: "
                            + transactionId);
        }

        return transactions.get(0);
    }

    @Override
    public Transaction updateTransactionStatus(
            String transactionId,
            UpdateTransactionStatusRequest request) {

        Transaction transaction =
                getTransaction(transactionId);

        TransactionStatus currentStatus =
                transaction.getStatus();

        TransactionStatus newStatus =
                request.getStatus();

        // COMPLETED, FAILED and CANCELLED are terminal states.
        if (currentStatus == TransactionStatus.COMPLETED
                || currentStatus == TransactionStatus.FAILED
                || currentStatus == TransactionStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Transaction status cannot be changed from "
                            + currentStatus);
        }

        transaction.setStatus(newStatus);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByCustomer(
            String customerId) {

        return transactionRepository
                .findByCustomerId(customerId);
    }
    
    
    @Override
    public void deleteTransaction(String transactionId) {

        Transaction transaction =
                getTransaction(transactionId);

        transactionRepository.delete(transaction);
    }
    
    
    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }
    
    @Override
    public List<String> getAllCustomers() {

        return transactionRepository.findAll()
                .stream()
                .map(Transaction::getCustomerId)
                .distinct()
                .toList();
    }

}