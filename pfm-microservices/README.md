# Personal Finance Manager — Microservices (Spring Boot + REST)



**Modules & Ports**
- `budget-service` (8081): Manage budgets per user & category; apply expenses and report exceedance.
- `expense-service` (8082): Record expenses; talks to budget-service and, on exceedance, calls notification-service.
- `notification-service` (8083): Stores notifications when a budget is exceeded.

**Stack**: Java 17, Spring Boot 3.3.x, Spring Data JPA, H2, Bean Validation, springdoc OpenAPI, RestClient.

## Run locally
Open 3 terminals from the project root and run:
```bash
mvn -q -pl budget-service -am spring-boot:run
mvn -q -pl notification-service -am spring-boot:run
mvn -q -pl expense-service -am spring-boot:run
```
Swagger UIs:
- http://localhost:8081/swagger-ui.html
- http://localhost:8082/swagger-ui.html
- http://localhost:8083/swagger-ui.html

H2 consoles:
- http://localhost:8081/h2 (JDBC `jdbc:h2:mem:budget-service`)
- http://localhost:8082/h2 (JDBC `jdbc:h2:mem:expense-service`)
- http://localhost:8083/h2 (JDBC `jdbc:h2:mem:notification-service`)

## Quick test flow
1. Create a budget (budget-service).
2. Post an expense (expense-service) whose amount exceeds the budget.
3. List notifications (notification-service) by `userId`.

Import the Postman collection from `postman/PersonalFinanceManager.postman_collection.json`.
