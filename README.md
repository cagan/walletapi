# Wallet API

A Spring Boot application for managing digital wallets, handling deposits, withdrawals, and transaction management with role-based access control.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Flow](#api-flow)
- [Default User Credentials](#default-user-credentials)
- [API Documentation](#api-documentation)

## Features

- User authentication with JWT
- Wallet creation and management
- Transaction processing (deposits and withdrawals)
- Transaction approval workflow
- Role-based access control (Customer and Employee roles)
- Search functionality for wallets
- PostgreSQL database integration

## Tech Stack

- Java & Spring Boot
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven

## Project Structure

The project follows a standard Spring Boot application structure with the following main packages:

- `config`: Application configurations including Security and OpenAPI
- `data`: Database related components
  - `entity`: JPA entities
  - `repository`: Spring Data repositories
  - `spec`: Specifications for complex queries
- `dto`: Data Transfer Objects
- `error`: Error handling
- `mapper`: Object mappers
- `rest`: REST API components
  - `controller`: REST controllers
  - `request`: Request models
  - `response`: Response models
- `security`: Security related components
- `service`: Business logic
  - `balance`: Balance management
  - `customer`: Customer management
  - `deposit`: Deposit operations
  - `transaction`: Transaction processing
  - `wallet`: Wallet operations
  - `withdraw`: Withdrawal operations
- `util`: Utility classes
  - `enums`: Application enums
  - `initializer`: Data initialization
- `validator`: Custom validators

## Getting Started

### Prerequisites

- Docker and Docker Compose
- JDK 17 or newer
- Maven

### Running with Docker

The easiest way to run the application is using the provided `run.sh` script:

```bash
chmod +x run.sh
./run.sh
```

This script will:
1. Kill any existing Docker Compose services
2. Build the application with Maven
3. Create a Docker image
4. Start the application with Docker Compose

Alternatively, you can run the commands manually:

```bash
# Build the application
mvn clean package -DskipTests

# Build the Docker image
mvn compile jib:dockerBuild

# Start the containers
docker-compose up -d
```

The application will be available at http://localhost:8080

## API Flow

The typical flow of the API is as follows:

1. **Authentication**: Users authenticate using the `/auth/login` endpoint to obtain a JWT token
2. **Wallet Creation**: Authenticated users can create wallets via the `/api/v1/wallets` endpoint
3. **Deposit/Withdraw**: Users can deposit or withdraw funds using the respective endpoints
4. **Transaction History**: Users can view their transaction history for a specific wallet
5. **Transaction Approval**: Employees can approve or deny transactions via the approval endpoint

### Main API Endpoints

- **Authentication**: `POST /auth/login`
- **Wallet Management**:
  - Create wallet: `POST /api/v1/wallets`
  - Search wallets: `POST /api/v1/wallets/search`
- **Transactions**:
  - Get transactions: `GET /api/v1/transactions/{walletId}`
  - Approve transaction: `POST /api/v1/transactions/approval`
- **Deposits**: `POST /api/v1/deposits`
- **Withdrawals**: `POST /api/v1/withdraws`

## Default User Credentials

The application initializes with two default users:

1. **Customer User**
   - Username: `test`
   - Password: `123456`
   - Role: `CUSTOMER`

2. **Admin User**
   - Username: `admin`
   - Password: `123456`
   - Role: `EMPLOYEE`

## API Documentation

The API is documented using OpenAPI/Swagger. Once the application is running, you can access the API documentation at:

```
http://localhost:8080/swagger-ui.html
```

Authentication uses JWT Bearer tokens. After logging in, include the token in the Authorization header for all subsequent requests:

```
Authorization: Bearer <your-jwt-token>
```