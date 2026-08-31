package com.example.transactionstarter;

import com.example.transactionstarter.model.Transaction;
import com.example.transactionstarter.model.TransactionStatus;
import com.example.transactionstarter.model.TransactionType;
import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionStarterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransaction() throws Exception {

        String request = """
                {
                    "transactionId": "TXN1001",
                    "customerId": "CUST001",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT"
                }
                """;

        mockMvc.perform(
                post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message")
                .value("Transaction successfully created"))
        .andExpect(jsonPath("$.transactionId")
                .value("TXN1001"));
    }

    @Test
    void shouldGetTransaction() throws Exception {

        Transaction transaction = new Transaction(
                "TXN1002",
                "CUST001",
                new BigDecimal("500.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.PENDING
        );

        transactionRepository.save(transaction);

        mockMvc.perform(
                get("/api/transactions/TXN1002")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message")
                .value("Transaction successfully retrieved"))
        .andExpect(jsonPath("$.transactionId")
                .value("TXN1002"));
    }


    // 3. GET NON-EXISTING TRANSACTION
    @Test
    void shouldHandleTransactionNotFound() throws Exception {

        mockMvc.perform(
                get("/api/transactions/DOES_NOT_EXIST")
        )
        .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateTransactionStatus() throws Exception {

        Transaction transaction = new Transaction(
                "TXN1003",
                "CUST001",
                new BigDecimal("750.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.PENDING
        );

        transactionRepository.save(transaction);

        String request = """
                {
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(
                patch("/api/transactions/TXN1003/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message")
                .value("Transaction status successfully updated"))
        .andExpect(jsonPath("$.transactionId")
                .value("TXN1003"));
    }


    @Test
    void shouldGetTransactionsByCustomer() throws Exception {

        Transaction transaction1 = new Transaction(
                "TXN1004",
                "CUST001",
                new BigDecimal("100.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.PENDING
        );

        Transaction transaction2 = new Transaction(
                "TXN1005",
                "CUST001",
                new BigDecimal("200.00"),
                "INR",
                TransactionType.TRANSFER,
                TransactionStatus.PENDING
        );

        Transaction transaction3 = new Transaction(
                "TXN1006",
                "CUST002",
                new BigDecimal("300.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.PENDING
        );

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        mockMvc.perform(
                get("/api/customers/CUST001/transactions")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()")
                .value(2));
    }

    @Test
    void shouldRejectDuplicateTransaction() throws Exception {

        Transaction transaction = new Transaction(
                "TXN1007",
                "CUST001",
                new BigDecimal("500.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.PENDING
        );

        transactionRepository.save(transaction);

        String request = """
                {
                    "transactionId": "TXN1007",
                    "customerId": "CUST002",
                    "amount": 1000.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT"
                }
                """;

        mockMvc.perform(
                post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
        .andExpect(status().isConflict());
    }


    // 7. TERMINAL STATUS CANNOT BE CHANGED
    @Test
    void shouldNotUpdateCompletedTransaction() throws Exception {

        Transaction transaction = new Transaction(
                "TXN1008",
                "CUST001",
                new BigDecimal("900.00"),
                "INR",
                TransactionType.PAYMENT,
                TransactionStatus.COMPLETED
        );

        transactionRepository.save(transaction);

        String request = """
                {
                    "status": "FAILED"
                }
                """;

        mockMvc.perform(
                patch("/api/transactions/TXN1008/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
        .andExpect(status().isNotFound());
    }
}