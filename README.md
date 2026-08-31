# Transaction Starter Project

This is the starter project for the Customer Transactions exercise.

## Before you start

The first thing you should do after cloning the repository is:

### Linux / macOS

```bash
./mvnw clean test
```

### Windows

```bat
mvnw.cmd clean test
```

The sample test should pass before you begin implementing the exercise.

# What is already provided

* Java 17
* Spring Boot
* Maven wrapper
* Spring Web
* Spring Data JPA
* H2 embedded database
* JUnit / Spring Boot Test
* A sample REST endpoint: `GET /api/sample`
* A sample test that loads the Spring context





# Documentation



#### Understanding of the Problem



The project is a Spring Boot REST API designed to manage customer transactions. It allows users to create transactions, retrieve transactions by ID, retrieve transactions by customer, and update transaction status

#### Assumptions Made

•  Each transaction has a unique transactionId.

•  Each transaction belongs to a customerId.

•  Transaction amount must be valid and positive.

•  Currency, transaction type, and status must use supported values.

•  New transactions normally start in PENDING status.

#### Validation Rules

The application uses Validation to validate incoming requests. Required fields such as transaction ID, customer ID, amount, currency, and transaction type must be provided. Invalid or missing values are rejected. Duplicate transaction IDs are not allowed, and invalid status transitions are prevented.


API Endpoints
---



Method	Endpoint	                                   Description

POST	/api/transactions	                          Create a transaction

GET	/api/transactions	                          Get all transactions

GET	/api/transactions/{transactionId}	          Get transaction by ID

PATCH	/api/transactions/{transactionId}/status	  Update transaction status

DELETE	/api/transactions/{transactionId}	          Delete a transaction

GET	/api/customers/{customerId}/transactions	  Get customer transactions

GET	/api/customers	Get all customers



#### Testing Approach

Testing was performed using JUnit 5, Spring Boot Test, and MockMvc. The test cases cover transaction creation, retrieval, status updates, customer-based retrieval, duplicate transaction handling, non-existing transactions, and prevention of updates to completed transactions. The database is cleared before each test to keep tests independent.



#### Known Limitations

The application currently uses an H2 in-memory database, so data is lost when the application stops. Authentication, authorization, pagination, advanced filtering, and production database configuration are not implemented. Error responses are also simple text messages rather than a standardized error format.

#### Improvements With More Time

I would add MySQL for persistent storage, authentication and role-based authorization, standardized error responses, pagination and filtering.

