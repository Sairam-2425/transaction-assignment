package com.example.transactionstarter.dto;

public class TransactionResponse {

    private String message;
    private String transactionId;

    public TransactionResponse(String message, String transactionId) {
        this.message = message;
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
