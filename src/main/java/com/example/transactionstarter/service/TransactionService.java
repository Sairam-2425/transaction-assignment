
package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import com.example.transactionstarter.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(CreateTransactionRequest request);

    Transaction getTransaction(String transactionId);

    Transaction updateTransactionStatus(
            String transactionId,
            UpdateTransactionStatusRequest request);

    List<Transaction> getTransactionsByCustomer(
            String customerId);

    void deleteTransaction(String transactionId);
    
    List<Transaction> getAllTransactions();

    List<String> getAllCustomers();
}

