# Ticket System

A REST API for managing customer support tickets, ticket status transitions, comments, searches, and aggregated ticket statistics.

## Overview

The service provides endpoints to:

- Create tickets associated with customers.
- Retrieve a ticket by its ID.
- Search tickets using optional filters.
- Retrieve all tickets belonging to a customer.
- Update a ticket status according to the supported workflow.
- Add and retrieve ticket comments.
- Retrieve aggregated ticket statistics.

The API uses a layered architecture based on controllers, services, repositories, entities, DTOs, mappers, and centralized exception handling.

## Technology Stack

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Jakarta Bean Validation
- Lombok
- MapStruct dependencies are configured for future mapper implementations

## Project Structure

```text
src/main/java/com/aldob
├── controllers       HTTP endpoints
├── dtos              Request and response objects
├── entities          JPA entities
├── enums             Ticket priority and status enums
├── exceptions        Global and custom exception handling
├── mappers           Entity-to-DTO and DTO-to-entity mappings
├── repositories      Spring Data JPA repositories
└── services          Business logic
```

## Running the Application

### Prerequisites

- Java 17 or later
- Maven
- PostgreSQL
- A PostgreSQL database named `develop`

The current local configuration uses:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/develop
    username: aldo
    password: admin
```

For a production deployment, database credentials should be provided through environment variables or a secrets manager instead of being stored in `application.yaml`.

### Start the application

```bash
mvn spring-boot:run
```

The application runs on port `8080` with the context path:

```text
/ticketsystem
```

Therefore, the base URL is:

```text
http://localhost:8080/ticketsystem
```

## API Endpoints

### Create a ticket

```http
POST /api/tickets
```

The `Idempotency-Key` header is required.

#### Headers

```http
Content-Type: application/json
Idempotency-Key: 4e7d8c12-9f35-4b4a-8d1b-123456789abc
```

#### Request

```json
{
  "customerId": 1,
  "title": "I cannot complete my purchase",
  "description": "The system displays an error when trying to pay.",
  "priority": "HIGH"
}
```

#### Response

```http
HTTP 201 Created
```

```json
{
  "success": true,
  "message": "Ticket created successfully",
  "data": {
    "id": 15,
    "customerId": 1,
    "title": "I cannot complete my purchase",
    "description": "The system displays an error when trying to pay.",
    "priority": "HIGH",
    "status": "OPEN",
    "createdAt": "2026-09-14T20:15:30Z",
    "updatedAt": "2026-09-14T20:15:30Z"
  }
}
```

The backend assigns:

- `status = OPEN`
- `createdAt = current timestamp`
- `updatedAt = current timestamp`

### Get a ticket by ID

```http
GET /api/tickets/{id}
```

Example:

```http
GET /api/tickets/15
```

### Search tickets

```http
GET /api/tickets
```

Supported optional filters:

```http
GET /api/tickets?status=OPEN
GET /api/tickets?priority=HIGH
GET /api/tickets?status=OPEN&priority=HIGH
GET /api/tickets?customerId=1
```

Available status values:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

Available priority values:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

The filters are optional. If no filter is provided, all tickets are returned.

### Get customer tickets

```http
GET /api/customers/{customerId}/tickets
```

Example:

```http
GET /api/customers/1/tickets
```

The endpoint returns the tickets associated with the specified customer.

### Update ticket status

```http
PATCH /api/tickets/{id}/status
```

#### Request

```json
{
  "status": "IN_PROGRESS"
}
```

#### Response

```http
HTTP 200 OK
```

```json
{
  "success": true,
  "message": "Status updated successfully",
  "data": {
    "id": 15,
    "status": "IN_PROGRESS",
    "updatedAt": "2026-09-14T21:00:00Z"
  }
}
```

Supported status transitions:

```text
OPEN        → IN_PROGRESS
IN_PROGRESS → RESOLVED
RESOLVED    → CLOSED
```

A ticket in `CLOSED` cannot return to `OPEN` or `IN_PROGRESS`.

Setting a ticket to its current status is allowed and is idempotent. For example, repeating `RESOLVED → RESOLVED` does not perform another update.

### Add a comment

```http
POST /api/tickets/{ticketId}/comments
```

#### Request

```json
{
  "authorName": "Support Level 1",
  "message": "We are reviewing the reported issue."
}
```

#### Response

```http
HTTP 201 Created
```

```json
{
  "success": true,
  "message": "Comment created successfully",
  "data": {
    "id": 25,
    "ticketId": 15,
    "authorName": "Support Level 1",
    "message": "We are reviewing the reported issue.",
    "createdAt": "2026-09-14T21:05:00Z"
  }
}
```

### Get ticket comments

```http
GET /api/tickets/{ticketId}/comments
```

Example:

```http
GET /api/tickets/15/comments
```

### Get ticket statistics

```http
GET /api/tickets/statistics
```

The statistics are calculated using an aggregated database query rather than issuing one query per counter.

Example response:

```json
{
  "success": true,
  "message": "Ticket statistics retrieved successfully",
  "data": {
    "totalTickets": 150,
    "openTickets": 35,
    "inProgressTickets": 20,
    "resolvedTickets": 70,
    "closedTickets": 25,
    "criticalTickets": 5
  }
}
```

## Idempotency

Ticket creation uses the `Idempotency-Key` request header to protect against duplicate creations caused by retries, double-clicks, network failures, or multiple application instances.

The key is stored in the database with a unique constraint:

```text
One logical operation → one Idempotency-Key → one ticket
```

If the same key is received again, the previously created ticket is returned instead of creating a duplicate.

The database must contain the idempotency column and unique index. For PostgreSQL, the equivalent schema change is:

```sql
ALTER TABLE tickets
ADD COLUMN idempotency_key VARCHAR(100);

CREATE UNIQUE INDEX uk_tickets_idempotency_key
ON tickets (idempotency_key)
WHERE idempotency_key IS NOT NULL;
```

## Validation and Error Handling

Request validation uses Jakarta Bean Validation. Invalid requests return `400 Bad Request`.

The `GlobalExceptionHandler` provides centralized handling for:

- Missing resources (`404 Not Found`)
- Invalid request data (`400 Bad Request`)
- Malformed JSON (`400 Bad Request`)
- Invalid enum values (`400 Bad Request`)
- Invalid status transitions (`409 Conflict`)
- Database constraint violations (`409 Conflict`)
- Database availability errors (`503 Service Unavailable`)
- Unexpected server errors (`500 Internal Server Error`)

Unexpected errors are logged with their full stack trace, while the client receives a generic message and a `traceId` for correlation.

## Design Considerations

- `@Transactional` is used for business operations that modify data.
- `@Transactional(readOnly = true)` is used for read operations.
- `@CreationTimestamp` and `@UpdateTimestamp` are used to manage audit timestamps.
- Status transitions are validated in the service layer.
- Database constraints provide the final protection against duplicate idempotency keys.
- DTOs prevent persistence entities from being exposed directly through the API.
- Repository queries perform filtering and aggregation in the database.

## Future Improvements

Potential production improvements include:

- Moving database credentials to environment variables or a secret manager.
- Adding database migrations with Flyway or Liquibase.
- Adding authentication and authorization.
- Adding optimistic locking with `@Version` for concurrent updates.
- Adding integration and repository tests.
- Adding OpenAPI/Swagger documentation.
- Adding structured JSON logging and distributed tracing.
