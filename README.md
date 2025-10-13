# Library Manager

A RESTful Library Manager application built with Java, Spring Boot, and Maven. It provides secure endpoints for managing users and books, including borrowing and returning functionality, with admin authorization for sensitive operations.

## Features

- User management (CRUD, registration, login)
- Book management (CRUD, borrow, return, overdue fine calculation)
- Admin authorization for protected actions
- Password hashing (BCrypt)
- JWT-based authentication and authorization
- Input validation and exception handling
- RESTful API design
- SwaggerUI

## Technologies

- Java 21
- Spring Boot
- Maven
- JPA/Hibernate
- BCrypt (password security)
- JWT for authentication
- SwaggerUI

## Project Structure

- `Controllers/` — REST API controllers for users and books
- `Models/` — Entity classes (`User`, `Book`)
- `Services/` — Business logic and service implementations
- `AppDataContext/` — JPA repositories
- `Middleware/` — Custom exceptions and validation
- `Security/` — JWT utilities and filters
- `UI/` — SwaggerUI configuration
  
## Getting Started

### Prerequisites

- Java 21
- Maven

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```
## SwaggerUI

After running the application, use the link below to explore and test all endpoints interactively: 
`http://localhost:8080/swagger-ui/index.html`

### API Endpoints

#### Authoritization

- `POST /login` — Logs the user in and gives him a bearer token

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
