# Nida Butik

Nida Butik is a Spring Boot backend for a boutique jewelry and womens fashion shop. It uses PostgreSQL, JPA, DTOs, validation, Spring Security RBAC, transaction management, and a Shopify-style static storefront.

## Tech Stack

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- PostgreSQL
- Docker Compose
- Maven Wrapper
- H2 for tests

## Architecture

- `entity`: database models
- `repository`: CRUD and derived queries
- `service`: business rules and transactions
- `controller`: HTTP endpoints
- `dto`: request and response objects
- `exception`: centralized error handling
- `config`: security and bootstrap data

## Database Design

The schema follows 3NF principles and includes:

- `customers`
- `products`
- `brands`
- `categories`
- `suppliers`
- `orders`
- `order_items`
- `payments`

`products` references `brands`, `categories`, and `suppliers`. `orders` and `order_items` represent the sales flow. `payments` is linked one-to-one with an order.

## Design Guide

The frontend styling follows the rules in [`shopify.com-DESIGN.md`](./shopify.com-DESIGN.md).

## Requirements Covered

- Entity, repository, service, controller packages are present.
- DTO and validation annotations are used on request payloads.
- Global exception handling is implemented in `exception`.
- Derived query methods are used in repository interfaces.
- Spring Security RBAC is enabled with `USER` and `ADMIN` roles.
- Payment flow is wrapped in transactional service methods with rollback behavior.
- Static product imagery is bundled under `src/main/resources/static/images`.

## Run With Docker

Start PostgreSQL:

```powershell
docker compose up -d
```

Run the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

```text
http://localhost:8080
```

Run tests:

```powershell
.\mvnw.cmd test
```

## Default Users

- `user` / `1234`
- `admin` / `admin123`

Permissions:

- `GET /api/**`: `USER` or `ADMIN`
- `POST /api/**`: `ADMIN`
- `PUT /api/**`: `ADMIN`
- `DELETE /api/**`: `ADMIN`

## Useful API Endpoints

- `GET /api/products`
- `GET /api/products/filter?minPrice=1000&maxPrice=3000&model=Elbise&brand=Zara%20Studio`
- `GET /api/customers/top-buyers?gender=FEMALE`
- `POST /api/orders`
- `POST /api/payments`
- `POST /api/payments/rollback-demo`

## Postman

Import the collection and environment from the `postman` folder:

- `postman/Nida-Butik.postman_collection.json`
- `postman/Nida-Butik.postman_environment.json`

The collection already includes the base URL and Basic Auth values for the default `user` and `admin` accounts.

## Demo Data

At startup the app seeds:

- multiple customers for male and female top-buyer queries
- boutique products with category, brand, and supplier relations
- paid and unpaid sample orders
- payment rows for completed orders

This makes the project easier to demonstrate in IntelliJ, the browser, and Postman.
