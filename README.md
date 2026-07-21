# Time Scheduler API

A RESTful API for appointment and schedule management built with **Spring Boot**, featuring **JWT authentication**, **role-based authorization (USER/ADMIN)**, and automatic API documentation with **Swagger/OpenAPI**.

This project was developed to demonstrate modern backend development practices using the Spring ecosystem, focusing on clean architecture, security, and maintainability.

---

# Features

## Authentication

- User registration
- JWT-based authentication
- Access Token
- Refresh Token
- Secure logout
- Token revocation using token versioning

## Users

- Retrieve user information by username
- Role-based authorization
  - USER
  - ADMIN

## Scheduling

- Create appointments
- Update appointment time
- Delete appointments
- Retrieve appointments by day
- Prevent scheduling conflicts for the same worker

---

# Security

The API uses **Spring Security** with JWT authentication.

Implemented security features include:

- Stateless authentication
- Password hashing with BCrypt
- JWT Access Tokens
- JWT Refresh Tokens
- Refresh Tokens stored only as SHA-256 hashes
- Global session invalidation through `tokenVersion`
- Role-based authorization
- Custom JWT authentication filter

---

# Architecture

The project follows a layered architecture:

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Project structure:

```
config/
controller/
dto/
entity/
exceptions/
mapper/
repository/
security/
services/
```

---

# Technologies

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT (JJWT)
- H2 Database
- Swagger / OpenAPI
- Lombok
- Maven

---

# Project Structure

```
src
 ├── config
 ├── controller
 ├── dto
 ├── entity
 ├── exceptions
 ├── mapper
 ├── repository
 ├── security
 ├── services
 └── TimeschedulerApplication
```

---

# Authentication Flow

```text
User Login
      │
      ▼
Access Token + Refresh Token
      │
      ▼
Authenticated Requests
      │
      ▼
Refresh Token
      │
      ▼
New Access Token
      │
      ▼
Logout
      │
      ▼
Token Version++
All previously issued tokens become invalid
```

---

# API Documentation

After starting the application, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

# Database

The project uses an **H2 in-memory database** for development and testing.

H2 Console:

```
http://localhost:8080/h2-console
```

---

# Running the Project

Clone the repository:

```bash
git clone https://github.com/lmrick/timescheduler.git
```

Navigate to the project folder:

```bash
cd timescheduler
```

Run the application:

```bash
mvn spring-boot:run
```

Or execute the main class:

```
TimeschedulerApplication
```

---

# Main Endpoints

## Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Authenticate a user and return access and refresh tokens |
| `POST` | `/auth/refresh` | Generate a new access token using a refresh token |
| `POST` | `/auth/logout` | Invalidate the user's refresh token |

## Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users` | Retrieve paginated list of users |
| `GET` | `/users/{id}` | Retrieve a user by ID |
| `GET` | `/users/search?username={username}` | Retrieve a user by username |

## Scheduler

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/scheduler` | Create a new scheduler |
| `GET` | `/scheduler` | List all schedulers |
| `PUT` | `/scheduler/{id}` | Update a scheduler |
| `PATCH` | `/scheduler/{id}/status` | Update the scheduler status |
| `PATCH` | `/scheduler/{id}/phone` | Update the client phone number |
| `DELETE` | `/scheduler/{id}` | Delete a scheduler |

---

# Future Improvements

- :red_circle: Store the JWT secret using environment variables instead of keeping it in `application.properties`. The current hardcoded secret is included **only for development and testing purposes**.
- :red_circle: Replace the H2 in-memory database with PostgreSQL or MySQL for production.
- :red_circle: Add unit and integration tests.
- :green_circle: Implement pagination for list endpoints.
- :red_circle: Add audit logging.
- :red_circle: Create Docker and Docker Compose configurations.
- :red_circle: Configure CI/CD using GitHub Actions.

---

# 📄 License

This project was developed for educational purposes and as part of my software development portfolio.
