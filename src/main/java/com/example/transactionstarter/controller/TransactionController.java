package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.TransactionResponse;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import com.example.transactionstarter.model.Transaction;
import com.example.transactionstarter.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // CREATE
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction transaction =
                transactionService.createTransaction(request);

        TransactionResponse response =
                new TransactionResponse(
                        "Transaction successfully created",
                        transaction.getTransactionId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET BY TRANSACTION ID
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @PathVariable String transactionId) {

        Transaction transaction =
                transactionService.getTransaction(transactionId);

        TransactionResponse response =
                new TransactionResponse(
                        "Transaction successfully retrieved",
                        transaction.getTransactionId()
                );

        return ResponseEntity.ok(response);
    }

    // UPDATE STATUS
    @PatchMapping("/transactions/{transactionId}/status")
    public ResponseEntity<TransactionResponse> updateStatus(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateTransactionStatusRequest request) {

        Transaction transaction =
                transactionService.updateTransactionStatus(
                        transactionId,
                        request
                );

        TransactionResponse response =
                new TransactionResponse(
                        "Transaction status successfully updated",
                        transaction.getTransactionId()
                );

        return ResponseEntity.ok(response);
    }

    // GET BY CUSTOMER ID
    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomer(
            @PathVariable String customerId) {

        List<Transaction> transactions =
                transactionService.getTransactionsByCustomer(customerId);

        return ResponseEntity.ok(transactions);
    }
    
 // DELETE TRANSACTION
    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable String transactionId) {

        transactionService.deleteTransaction(transactionId);

        return ResponseEntity .status(HttpStatus.OK) .body("Transaction successfully deleted: " + transactionId);
        		
    }

 // VALIDATION ERRORS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }


 // TRANSACTION NOT FOUND
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(
            java.util.NoSuchElementException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    // DUPLICATE TRANSACTION
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    // INVALID STATUS TRANSITION
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(
            IllegalStateException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

}
