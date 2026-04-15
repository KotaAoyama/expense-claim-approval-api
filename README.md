# Expense Claim Approval API

## Overview
This is a Spring Boot based REST API for expense claim workflow management.

The application focuses on workflow consistency and business rules rather than simple CRUD operations.
It supports creating, editing, submitting, approving, and rejecting expense claims with explicit state transitions.

---

## Scope
This application focuses on expense claim workflow and business rules.

Authentication and authorization are intentionally out of scope for this version.  
The current implementation uses a fixed dummy user and prioritizes:

- state transitions
- validation
- workflow consistency
- exception handling

Therefore, all endpoints are currently accessible without authentication.

As a future improvement, authentication/authorization can be added with Spring Security.

---

## Features
- Create an expense claim
- List expense claims
- Get expense claim detail
- Edit a draft expense claim
- Submit a draft expense claim
- Approve a submitted expense claim
- Reject a submitted expense claim

---

## Workflow Rules
- A claim is created with `DRAFT` status
- A claim can be edited only when its status is `DRAFT`
- A claim can be submitted only when its status is `DRAFT`
- Once submitted, the claim is locked from editing
- A claim can be approved or rejected only when its status is `SUBMITTED`
- `reviewerComment` is handled only during approve/reject operations

---

## Status Transition

| Current Status | Operation | Next Status |
|---|---|---|
| DRAFT | submit | SUBMITTED |
| SUBMITTED | approve | APPROVED |
| SUBMITTED | reject | REJECTED |

Edit is allowed only in `DRAFT`.

---

## Validation Policy
Validation is handled mainly in the service layer.

- Input validation is performed when creating or editing an expense claim
- Additional validation is performed on submit to ensure required fields are present before state transition
- Some checks are also kept defensively in the entity to preserve workflow consistency

For example, `title` is validated during creation, so it is not expected to be missing during normal submit flow.  
However, submit still performs defensive validation to protect workflow integrity.

---

## API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/expense-claims` | Create a new expense claim |
| GET | `/expense-claims` | Get expense claim list |
| GET | `/expense-claims/{id}` | Get expense claim detail |
| PATCH | `/expense-claims/{id}` | Edit a draft expense claim |
| POST | `/expense-claims/{id}/submit` | Submit a draft expense claim |
| POST | `/expense-claims/{id}/approve` | Approve a submitted expense claim |
| POST | `/expense-claims/{id}/reject` | Reject a submitted expense claim |

---

## Tech Stack
- Java
- Spring Boot
- PostgreSQL

---

## Testing
The service layer is covered with tests for:

- normal workflow transitions
- invalid state transitions
- validation failures
- not found cases
- partial update behavior

---

## How to Run

```bash
docker compose up -d
./mvnw spring-boot:run
```

---

## API Documentation

Swagger UI is available at:
`http://localhost:8080/swagger-ui.html`
