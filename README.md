# 🛒 SmartCart

> A modular e-commerce backend built with **Java and Spring Boot**, focused on real-world backend development, JWT authentication, role-based authorization, REST API design, persistence, checkout calculations, order processing, and invoice generation.

## 📖 Overview

**SmartCart** is a backend-focused e-commerce application developed to practise modern Java backend engineering. It uses a modular, feature-based architecture and separates controllers, services, repositories, entities, DTOs, and mappers.

The current implementation covers the core shopping journey:

**Authentication → Product browsing → Cart management → Checkout preview → Order placement → Order history / cancellation → PDF invoice**

The project also contains the beginnings of a review-and-rating module, which is not yet implemented end to end.

---

# ✨ Features

## 🔐 Authentication & Security

- JWT-based authentication
- BCrypt password encryption
- Spring Security integration
- Custom JWT authentication filter
- Custom authentication entry point
- Method-level role-based authorization

### Roles

- 👤 `CUSTOMER`
- 🛍️ `SELLER`
- 👑 `ADMIN`

Public endpoints are limited to registration, login, and the configured Swagger/OpenAPI resources. Other application APIs are protected according to their security rules.

---

# 📦 Implemented Modules

## 1. User & Authentication Module

- User registration and login
- User profile operations
- Role management
- Authentication using JWT
- Password hashing with BCrypt

## 2. Address Module

- Create, retrieve, update, and delete addresses
- Address ownership is associated with the authenticated user
- Address data is used by checkout and order placement

## 3. Seller & Admin Module

- Seller profile registration and retrieval
- Seller profile update
- Admin access to user-management operations
- Admin seller verification workflow, including approval and rejection
- User blocking and unblocking operations

## 4. Category Module

- Create categories
- Retrieve all categories
- Retrieve a category by ID
- Update categories
- Delete categories
- Category management is restricted to administrators

## 5. Product Module

- Product catalog listing
- Product details retrieval
- Product search
- Pagination support
- Search/filter request DTO support
- Seller product creation and management
- Product status management
- Soft-delete support in the product service layer

## 6. Cart Module

- Retrieve the authenticated customer's cart
- Add a product to the cart
- Increase the quantity when an existing product is added again
- Update cart-item quantity
- Remove an individual cart item
- Clear the cart
- Customer-only access

## 7. Checkout Module

- Checkout preview for selected cart items
- Shipping-address ownership validation
- Validation that selected cart items belong to the customer's cart
- Duplicate cart-item ID rejection
- Product availability and stock validation
- Server-side price calculation
- Subtotal calculation
- GST calculation (currently configured at 5%)
- Delivery charge calculation (currently configured at INR 10.00)
- Discount calculation (currently configured as zero)
- Final payable amount calculation

Checkout is a preview/calculation step. Order creation recalculates checkout values again rather than trusting prices supplied by the client.

## 8. Order Module

- Place an order using selected cart items and a shipping address
- Generate a unique order number
- Store order-item snapshots, including product name, image, quantity, and unit price
- Store a shipping-address snapshot with the order
- Reduce product stock when an order is placed
- Mark products as out of stock when stock reaches zero
- Remove only the cart items included in the order
- Retrieve the authenticated customer's order history
- Retrieve an individual order's details
- Cancel eligible orders
- Restore product stock when an order is cancelled
- Restrict order APIs to customers and scope order retrieval to the authenticated customer

## 9. Invoice PDF Generation

- Generate an invoice PDF for an order
- Include order number, order status, customer details, shipping address, order items, and price breakdown
- Include subtotal, GST, delivery charge, discount, and grand total
- Download an invoice through the order API
- Use Apache PDFBox for PDF generation

The current invoice renderer is a basic one-page implementation; long orders may require a multi-page layout.

## 10. API Documentation

- OpenAPI / Swagger UI integration
- Endpoint descriptions and operation summaries on documented APIs

---

# 🚧 In Progress

## Review & Rating Module

The project currently contains review entities, DTOs, a repository, and service/controller scaffolding. The review service methods are not implemented yet, and the controller code is currently commented out.

Planned capabilities include:

- Create a product review
- Update a customer's own review
- Delete a customer's own review
- Retrieve reviews for a product
- Retrieve a product review summary
- Enforce verified-purchase and review-ownership rules

---

# 🔮 Future Roadmap

Potential future enhancements include:

- Wishlist
- Payment gateway integration
- Product image upload and storage
- Inventory management enhancements
- Advanced product filtering and sorting
- Notifications
- Cloud storage integration
- Deployment and production configuration

---

# 🛠️ Tech Stack

| Technology | Version / Usage |
|---|---|
| Java | 21 (Maven compiler target) |
| Spring Boot | 4.1.0 |
| Spring Security | 7.1.0 (managed by Spring Boot) |
| Maven | Maven Wrapper included |
| PostgreSQL | Runtime database |
| Docker | PostgreSQL container via Docker Compose |
| Spring Data JPA | Persistence and repository layer |
| JWT | Authentication |
| MapStruct | DTO/entity mapping |
| Lombok | Boilerplate reduction |
| Bean Validation | Request validation |
| Springdoc OpenAPI | API documentation |
| Apache PDFBox | Invoice PDF generation |
| JUnit & Mockito | Automated tests |

---

# 🗂️ Project Structure

```text
src
├── main
│   └── java/com/example/SmartCart
│       ├── Cart
│       │   ├── Controller
│       │   ├── Dto
│       │   ├── Entity
│       │   ├── Repo
│       │   └── Service
│       ├── Checkout
│       │   ├── Controller
│       │   ├── Dto
│       │   └── Service
│       ├── Mapper
│       │   ├── Cart
│       │   ├── Order
│       │   ├── Product
│       │   ├── Review
│       │   └── User
│       ├── Order
│       │   ├── Controller
│       │   ├── Dto
│       │   ├── Entity
│       │   ├── Enums
│       │   ├── Repository
│       │   └── Service
│       ├── Products
│       │   ├── Controller
│       │   ├── Dto
│       │   ├── Entity
│       │   ├── Repository
│       │   ├── Service
│       │   └── enums
│       ├── ReviewAndRatings
│       │   ├── Controller
│       │   ├── Dto
│       │   ├── Entity
│       │   ├── Repository
│       │   └── Service
│       ├── User
│       │   ├── Config
│       │   ├── Controller
│       │   ├── Dto
│       │   ├── Entity
│       │   ├── enums
│       │   ├── Filters
│       │   ├── Repository
│       │   ├── Security
│       │   └── Service
│       ├── common
│       │   ├── Config
│       │   ├── Entity
│       │   ├── Exception
│       │   └── Handler
│       └── SmartCartApplication.java
└── test
    └── java/com/example/SmartCart
        ├── Product/Controller
        ├── User/Controller
        ├── User/Service/Impl
        └── SmartCartApplicationTests.java
```

---

# 🗄️ Main Database Entities

The current source includes entities for:

- User
- Address
- SellerProfile
- Category
- Product
- ProductImage
- Cart
- CartItem
- Order
- OrderItem
- OrderAddressSnapshot
- Review

---

# 🏗️ Architecture Highlights

- Feature-based modular package structure
- Layered controller, service, repository, and entity design
- DTO-based request and response models
- MapStruct mapping for supported DTO/entity conversions
- Global exception handling
- Generic API response wrapper
- Bean Validation
- Auditable base entity (`createdAt`, `updatedAt`)
- Enum-based status management
- Transactional service operations
- Pessimistic product/order locking in critical order and stock operations
- Order-item and shipping-address snapshots to preserve order-time details
- Customer ownership checks for cart, checkout, and order operations

---

# 🔐 Authentication Flow

1. Register or log in.
2. Receive a JWT.
3. Include the token in protected API requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

4. Access endpoints permitted for the authenticated user's role.

---

# 🌐 API Information

The controllers define their resource paths (for example `/cart`, `/checkout`, `/orders`, `/products`, and `/categories`). The configured API prefix and server port should be checked in the application configuration for the active environment.

## Important Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/cart` | Retrieve the current customer's cart |
| POST | `/cart/items` | Add a product to the cart |
| PATCH | `/cart/items/{productId}` | Update cart-item quantity |
| DELETE | `/cart/items/{productId}` | Remove a cart item |
| DELETE | `/cart` | Clear the cart |
| POST | `/checkout/preview` | Calculate checkout totals |
| POST | `/orders` | Place an order |
| GET | `/orders` | Retrieve order history |
| GET | `/orders/{orderId}` | Retrieve order details |
| PATCH | `/orders/{orderId}/cancel` | Cancel an eligible order |
| GET | `/orders/{orderId}/invoice` | Download the order invoice PDF |
| GET | `/products` | Browse products |
| GET | `/products/{productId}` | Retrieve product details |
| POST | `/products/search` | Search products |
| GET | `/categories` | Retrieve categories |

## Swagger UI

After starting the application, open the Swagger UI path configured by the application:

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

---

# ⚙️ Getting Started

## Prerequisites

- JDK 21 or compatible newer JDK
- Docker with Docker Compose
- PostgreSQL (or use the provided Docker Compose configuration)
- Git

The Maven Wrapper is included, so a separate Maven installation is not required.

## Clone the Repository

```bash
git clone https://github.com/sutirthapaul17/SmartCart.git
cd SmartCart
```

## Configure the Application

Configure the database connection and application secrets in the relevant application configuration file or environment variables. In particular, provide:

- PostgreSQL URL
- Database username and password
- JWT signing secret and related JWT settings

Do not commit real credentials or signing secrets to version control.

## Start PostgreSQL

From the project root:

```bash
docker compose up -d
```

## Run Tests

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Linux / macOS:

```bash
./mvnw test
```

## Build and Run

Windows PowerShell:

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

---

# 📊 Current Project Status

| Module | Status |
|---|---|
| Authentication & Security | ✅ Implemented |
| User | ✅ Implemented |
| Address | ✅ Implemented |
| Seller & Admin | ✅ Implemented |
| Category | ✅ Implemented |
| Product Catalog | ✅ Implemented |
| Seller Product Management | ✅ Implemented |
| Cart | ✅ Implemented |
| Checkout | ✅ Implemented |
| Order Placement & History | ✅ Implemented |
| Order Cancellation & Stock Restoration | ✅ Implemented |
| Invoice PDF Generation | ✅ Implemented |
| API Documentation | ✅ Integrated |
| Review & Ratings | 🚧 Scaffolding present; implementation pending |
| Payment Gateway | ⏳ Planned |
| Wishlist | ⏳ Planned |
| Product Image Upload / Cloud Storage | ⏳ Planned |
| Notifications | ⏳ Planned |
| Deployment | ⏳ Planned |

---

# 🎯 Learning Objectives

- Spring Boot application architecture
- REST API design
- Spring Security and JWT authentication
- Role-based authorization
- PostgreSQL and Spring Data JPA
- Transaction management
- DTO mapping
- Bean Validation
- Global exception handling
- Cart and checkout workflows
- Order lifecycle and stock management
- PDF document generation
- Unit and controller security testing

---

# 👨‍💻 Author

Developed as a personal learning project to strengthen backend development skills with Spring Boot and modern Java technologies.

---

## 📄 License

This project is intended for educational and learning purposes.
