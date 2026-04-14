# expense-claim-approval-api

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
