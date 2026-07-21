# 🛒 SmartCart

> A modular e-commerce backend built with **Spring Boot** to practice real-world backend development, secure authentication, role-based authorization, REST API design, and scalable project architecture.

## 📖 Overview

**SmartCart** is a backend-focused e-commerce application developed as a learning project to gain hands-on experience with modern Java backend technologies. The project emphasizes clean architecture, modular design, Spring Security, JWT authentication, DTO mapping, validation, and maintainable REST APIs.

The current **Version 1** focuses on the core backend foundation, while advanced commerce features will be added in future versions.

---

# ✨ Features

## ✅ Authentication & Security
- JWT-based Authentication
- BCrypt Password Encryption
- Spring Security integration
- Custom JWT Security Filter
- Custom Authentication Entry Point
- Role-Based Access Control (RBAC)

### Roles
- 👤 CUSTOMER
- 🛍️ SELLER
- 👑 ADMIN

Only the following endpoints are public:
- User Registration
- User Login
- Swagger UI

All remaining APIs require authentication.

---

# 📦 Completed Modules

## User Module
- User Registration
- Login
- User Profile APIs
- Role Management

## Address Module
- Complete CRUD Operations

## Seller Module
- Seller Registration
- Seller Verification (Admin)

## Category Module
- Complete CRUD Operations

## Product Module
- Product CRUD
- Product Search
- Pagination Support
- Product Status Management
- Soft Delete

## API Documentation
- Swagger UI Integration

---

# 🚧 In Progress

- Cart Module
- Order Module
- Review & Rating APIs (Entities & DTOs completed)

---

# 🔮 Future Roadmap (Version 2)

- Wishlist
- Payment Gateway Integration
- Product Image Upload
- Inventory Management
- Filtering
- Sorting
- Notifications
- Cloud Storage Integration
- Deployment

---

# 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Security | *(Add Version)* |
| Maven | Latest |
| PostgreSQL | Latest |
| Docker | PostgreSQL Container |
| Spring Data JPA | ✔ |
| JWT | ✔ |
| MapStruct | ✔ |
| Lombok | ✔ |
| Bean Validation | ✔ |
| Swagger UI | ✔ |

---

# 🗂️ Project Structure

```text
src/main/java
├── common
│   ├── entity
│   ├── exception
│   ├── mapper
│   ├── response
│   └── utility
├── user
│   ├── config
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   ├── security
│   └── service
├── product
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
├── reviewrating
├── cart
└── ...
```

---

# 🗄️ Database Entities

- User
- Address
- SellerProfile
- Category
- Product
- ProductImage
- Review

---

# 🏗️ Architecture Highlights

- Modular feature-based package structure
- DTO ↔ Entity mapping using MapStruct
- Global Exception Handling
- Generic API Response Wrapper
- Bean Validation
- Auditable BaseEntity (`createdAt`, `updatedAt`)
- Soft Delete Support
- Enum-based Status Management

---

# 🔐 Authentication Flow

1. Register/Login
2. Receive JWT
3. Include token in:

```http
Authorization: Bearer <JWT_TOKEN>
```

4. Access protected endpoints according to assigned role.

---

# 🌐 API Information

**Base URL**

```text
http://localhost:8080/api/v1
```

## Swagger UI

After starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ Getting Started

## Prerequisites

- Java 21
- Maven
- Docker
- PostgreSQL

## Clone

```bash
git clone <repository-url>
cd SmartCart
```

## Configure

Update `application.properties` with your PostgreSQL credentials.

## Run PostgreSQL

Start the PostgreSQL Docker container.

## Start Application

```bash
mvn clean install
mvn spring-boot:run
```

---

# 📊 Current Status

| Module | Status |
|--------|--------|
| Authentication | ✅ |
| User | ✅ |
| Address | ✅ |
| Seller | ✅ |
| Category | ✅ |
| Product | ✅ |
| Search | ✅ |
| Pagination | ✅ |
| Swagger | ✅ |
| Cart | 🚧 |
| Orders | 🚧 |
| Reviews | 🚧 |

---

# 🎯 Learning Objectives

- Spring Boot Architecture
- REST API Design
- Spring Security
- JWT Authentication
- Role-Based Authorization
- PostgreSQL Integration
- Docker Basics
- DTO Mapping
- Clean Code Practices
- Exception Handling
- Validation

---

# 👨‍💻 Author

Developed as a personal learning project to strengthen backend development skills with Spring Boot and modern Java technologies.

---

## 📄 License

This project is intended for educational and learning purposes.
