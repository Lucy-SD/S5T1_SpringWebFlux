# 🎮 Spring Boot Reactive API - Blackjack Game

**👨‍💻 Author:** Lucy Castro

**🧠 Learning Focus:** Spring Boot WebFlux, Reactive APIs, Layered Architecture, TDD, Docker

**🛠️ IDE:** IntelliJ IDEA

**☕ Java SDK:** 21

**📦 Build Tool:** Maven


## 📝 Description

This project is a reactive Java API built with Spring Boot for managing Blackjack games. The application connects to two different databases: MySQL for relational data and MongoDB for document storage, implementing a complete Blackjack game with player management, card handling, and game rules.

The API follows reactive programming principles using Spring WebFlux and includes comprehensive documentation, testing, and Docker containerization.

## 💻 Technologies Used
- ☕ Java 21

- 🌱 Spring Boot 3.x

- ⚡ Spring WebFlux (Reactive programming)

- 🗄️ MySQL (Relational database)

- 🍃 MongoDB (Document database)

- 🧪 JUnit 5 & Mockito (Testing)

- 📚 Swagger/OpenAPI (Documentation)

- 🐳 Docker

- 📦 Maven


## 🎯 Learning Objectives

Develop reactive applications with Spring WebFlux

Configure and use multiple databases (MySQL + MongoDB)

Implement global exception handling

Create comprehensive API documentation with Swagger

Write unit tests with JUnit and Mockito

Containerize Spring Boot applications with Docker

Implement RESTful API best practices

## 🎲 Game Features
Create new Blackjack games

Manage player hands and game state

Implement Blackjack rules and gameplay

Player ranking system

Real-time game updates

## 🚀 API Endpoints
### Game Management
Method	Endpoint	Description	Success Response
POST	/game/new	Create new Blackjack game	201 Created
GET	/game/{id}	Get game details	200 OK
POST	/game/{id}/play	Make a game move	200 OK
DELETE	/game/{id}/delete	Delete a game	204 No Content
### Player Management
Method	Endpoint	Description	Success Response
GET	/ranking	Get player rankings	200 OK
PUT	/player/{playerId}	Update player name	200 OK

## 🏗️ Architecture

**Onion Architecture**

The design of the architecture is based and structure acording to ->

Each Module is divided into:

### Domain Layer (Core)
- Contains: entities, value objects, domain services, business rules, and interfaces of repositories, domain events and enums.
- No dependency on any other layer.
- Pure business logic.
### Application Layer
- The logic involves multiple entities or value objects.
- The behavior is business-critical, but not tied to a specific entity.
- It implements specific use cases of the system (e.g., register user, transfer funds).
- Coordinates business operations using the domain layer.
- Uses domain entities and services to achieve a goal.
- Depends only on the domain layer.
- Mapping between domain entities and database tables.
- Input validation, request/response mapping
### Infrastructure Layer
It sends commands or queries to the Application/Domain layer and presents data back to the user.
Contains implementations for external concerns like:
- Database access (persistence technology like JPA, JDBC, MongoDB, etc.)
- It implements interfaces defined in inner layers (usually in the Domain or Application layer).
- Persistence layer as a part of the Infrastructure layer specifically responsible for:
    - Data storage and retrieval
    - Usually contains repository implementationss.
- API endpoints (REST controllers)

### Database Configuration
MySQL: Stores relational game data and player information

MongoDB: Stores document-based game states and session data

### Reactive Implementation
Fully reactive using Spring WebFlux

Non-blocking I/O operations

Reactive MongoDB driver

Backpressure handling

### Global Exception Handling
Centralized error management with GlobalExceptionHandler

Consistent error responses

Proper HTTP status codes

## 🧪 Testing Strategy
Unit Tests: Service and controller layer testing with JUnit and Mockito

Reactive Testing: Testing reactive streams and publishers

Integration Tests: Full application testing with embedded databases

TDD Approach: Test-driven development methodology

## 📚 API Documentation
Swagger UI: Interactive API documentation available at /swagger-ui.html

OpenAPI Specification: Machine-readable API spec at /v3/api-docs

Complete endpoint documentation with request/response examples

## 🐳 Docker Deployment
Prerequisites
Docker installed and running

Docker Hub account (optional)

Steps to Deploy
Build the Docker image:

bash
docker build -t blackjack-api .

Run the container:

bash
docker run -p 8080:8080 blackjack-api

Tag for Docker Hub:

bash
docker tag blackjack-api lucysd/blackjack-api:latest

Push to Docker Hub:

bash
docker push lucysd/blackjack-api:latest

Dockerfile Structure
dockerfile

### Multi-stage build for optimized image size
FROM openjdk:21-jdk-slim as builder
### Build stage
FROM openjdk:21-jdk-slim
### Final stage with minimal footprint
### 🚀 Running the Application
Local Development
bash
./mvnw spring-boot:run
Production
bash
java -jar target/blackjack-api.jar
## ⚙️ Configuration
Environment Variables
SPRING_DATASOURCE_URL: MySQL connection URL

SPRING_DATA_MONGODB_URI: MongoDB connection string

SERVER_PORT: Application port (default: 8080)

Application Properties
Reactive MongoDB configuration

MySQL connection pooling

Swagger documentation settings

Server configuration

## 🎯 Key Features Implemented

✅ Reactive Programming with Spring WebFlux

✅ Dual Database Support (MySQL + MongoDB)

✅ Global Exception Handling

✅ Comprehensive API Documentation with Swagger

✅ Unit & Integration Tests

✅ Docker Containerization

✅ RESTful API Design

✅ Blackjack Game Logic

✅ Player Ranking System

✅ Reactive Data Access


## 🤝 Contributions
### ⭐ Star the repository
### 🍴 Fork the project
### 📥 Create a pull request

## 🌐 Deployment
For educational purposes only.

## 🚀 Thanks for Visiting! = )
