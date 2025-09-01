🚀 Project Management API

A secure, production-ready Spring Boot 3.5 (LTS) API for managing projects and tasks, with OAuth2 (Microsoft Identity) authentication.
Built as a monolithic application with a layered architecture, following REST best practices and enriched with lightweight features to demonstrate real-world readiness.

(
    application.yml, since it contains sensitive information, i didn't want to push it . download the file and place it in \src\main\resources
    link : https://www.swisstransfer.com/d/96b462ea-8fbe-489f-8d1f-65ec6f8d6ca0
    )

✨ Key Features

🔒 OAuth2 Security with Microsoft Outlook login

👤 User ownership enforcement for project and task actions

📊 User Progress tracking (% of completed tasks)

📝 Activity log of project/task update/create events

🎯 Global exception handling with consistent error responses (ProblemDetail)

📚 Postman Collection (shared workspace) for testing endpoints with one click + provided response examples

✅ REST best practices: correct HTTP codes, DTOs, validation

🛠️ Tech Stack

Java 21 → modern, long-term supported, performance improvements

Spring Boot 3.5 (LTS) → stable foundation for enterprise APIs

PostgreSQL → production-grade relational DB

Spring Security (Resource Server) → OAuth2/JWT validation

DTOs → prevent overexposing entities, improve maintainability and API contract clarity

🔒 Authentication

The API uses OAuth2 with Microsoft Identity:

Tokens are acquired directly in Postman.

👉 To test: in the Postman collection, oauth2.0 endpoint, click “Get New Access Token”, log in with your Microsoft account, then you can retreive the Access token from the popup that will appear

📡 API Endpoints

All endpoints are already documented and shared in the Postman team workspace.
They cover:

CRUD for projects and tasks

Project progress calculation

Current user profile (/me)

⚖️ Design Choices

Monolithic architecture → simple, self-contained, perfect for this scope.

Layered architecture → clear separation of concerns: Domain → Controller → Service → Repository.

DTOs → explicit input/output contracts, validation with @Valid, keep entities clean.

GlobalExceptionHandler → @RestControllerAdvice centralizes error handling for consistency.

Ownership checks → enforced in service layer to ensure only project owners can update/delete resources.