package com.example.transactionstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionStarterApplication.class, args);
        
        
        System.out.println( " Customer Transaction Project is running successfully!" ); 
        System.out.println( " Application URL: http://localhost:8080" );
    }
}
