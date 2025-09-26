# Library Manager

A RESTful Library Manager application built with Java, Spring Boot, and Maven. It provides secure endpoints for managing users and books, including borrowing and returning functionality, with admin authorization for sensitive operations.

## Features

- User management (CRUD, registration, login)
- Book management (CRUD, borrow, return, overdue fine calculation)
- Admin authorization for protected actions
- Password hashing (BCrypt)
- Input validation and exception handling
- RESTful API design

## Technologies

- Java 17+
- Spring Boot
- Maven
- JPA/Hibernate
- BCrypt (password security)

## Project Structure

- `Controllers/` — REST API controllers for users and books
- `Models/` — Entity classes (`User`, `Book`)
- `Services/` — Business logic and service implementations
- `AppDataContext/` — JPA repositories
- `Middleware/` — Custom exceptions and validation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

## API runs at `http://localhost:8080`

### API Endpoints

#### Users

- `GET /users` — List all users
- `GET /users/{id}` — Get user by ID
- `POST /users?adminId={adminId}` — Create user (admin required)
- `PUT /users/{id}?adminId={adminId}` — Update user (admin required)
- `DELETE /users/{id}?adminId={adminId}` — Delete user (admin required)

#### Books

- `GET /books?userId={userId}` — List available books for user
- `GET /books/author/{author}?userId={userId}` — List available books by author
- `POST /books/borrow/{bookId}?userId={userId}` — Borrow a book
- `POST /books/return/{bookId}?userId={userId}` — Return a book
- `POST /books?userId={userId}` — Add book (admin required)
- `DELETE /books/{bookId}?userId={userId}` — Remove book (admin required)

### Example Requests

**Register a user (admin required):**
```bash
curl -X POST http://localhost:8080/users?adminId=1 \
  -H "Content-Type: application/json" \
  -d '{"name": "John", "password": "pass123", "admin": false}'
```

**Borrow a book:**
```bash
curl -X POST http://localhost:8080/books/borrow/5?userId=2
```

**Return a book:**
```bash
curl -X POST http://localhost:8080/books/return/5?userId=2
```
