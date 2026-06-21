# 🏋️‍♂️ Kinect Tech

The **Kinect Tech** is a modern gym management platform designed with high scalability, availability, and strict isolation of business contexts in mind. This project is built using advanced enterprise architecture patterns, simulating a real-world, high-performance microservices environment.

---

## 🏛️ 1. System Architecture

The ecosystem is based on a **Microservices** architecture, adhering to the **API First** (Design-First) philosophy. It implements **Hexagonal Architecture (Ports & Adapters)** combined with **Clean Architecture** guidelines and **DDD (Domain-Driven Design)** principles.

### 🔄 Project Components & Flow
* **`kinect-api-contracts`**: A shared library that centralizes and distributes API contracts automatically generated via OpenAPI/Swagger.
* **`kinect-orchestrator`**: The BFF (Backend-For-Frontend) tasked with unifying communication with the Frontend (Angular) and centralizing JWT security.
* **`kinect-persons`**: An isolated microservice responsible for managing users (Students, Trainers/Personals, and Admins) and health questionnaires.
* **`kinect-training-programs`**: A microservice dedicated exclusively to the management of workout routines and sheets.
* **`kinect-payments`**: A microservice handling plans, catalogs, and subscriptions.

---

## 🛠️ 2. Tech Stack

* **Language:** Java 25 (Modern features and maximum performance)
* **Core Framework:** Spring Boot 3+ (Data JPA, Security, Web)
* **Database:** PostgreSQL (Using a *Database-per-service* strategy with 3 isolated physical databases)
* **Contract Generation:** OpenAPI Generator Maven Plugin (Automated generation of Java Records and Interfaces)
* **Layer Mapping:** MapStruct & Lombok
* **Dependency Management:** Maven

---

## 🚀 3. Getting Started Locally

### 📋 Prerequisites
* **Java 25** installed and configured in your environment PATH.
* **Maven 3.9+** installed.
* **PostgreSQL** running locally on the default port (`5432`).

### 🗄️ Step 1: Database Setup
Make sure to create the three databases in your local PostgreSQL instance using the default credentials configured in the local profile:
* `kinect_persons_db`
* `kinect_training_db`
* `kinect_payments_db`

### ⚙️ Step 2: Compiling the Shared Contracts Library
Since the microservices rely heavily on the Swagger-generated contracts, you must compile and publish the library to your local Maven repository (`.m2`) first. Open your terminal and run:

```bash```
cd kinect-api-contracts
mvn clean install

### 🏃‍♂️ Step 3: Running a Microservice (Example: kinect-persons)

Remember create .env files for your enviroment variables
cd ../kinect-persons
mvn spring-boot:run -Dspring-boot.run.profiles=local


### 🧪 4. Testing the API (Persons Module)
Once the application is up and running, you can test user creation by firing the following curl command directly in your terminal:

curl -X POST http://localhost:8081/api/v1/persons \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "secretPassword123",
    "name": "John Doe",
    "birthDate": "1995-06-15",
    "gender": "M",
    "personType": "ALUNO",
    "cpf": "12345678901",
    "email": "johndoe@email.com",
    "internalPersonal": false,
    "note": "Beginner student focused on hypertrophy."
  }'

  Expected Response: HTTP Status 201 Created (Empty body).
