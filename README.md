# Mana Rythu — Backend API

Spring Boot REST API for the first booking MVP. This directory is an independent Git repository from `harvester-frontend`.

## Requirements

- Java 21
- Maven 3.9+
- Docker Desktop (for the included local MySQL service)

## Run locally

1. Start MySQL from this directory with `docker compose up -d`.
2. Start the API with `mvn spring-boot:run`.
3. Check `http://localhost:8080/api/health`.

For a quick in-memory demo without Docker, run `mvn spring-boot:run -Dspring-boot.run.profiles=local`. The local profile uses H2 and resets its data whenever the process stops.

The first run inserts three demo slots for tomorrow at ₹2,500 per trip. The seeded sample is only inserted into an empty slots table. Database settings can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

## API endpoints

- `GET /api/health`
- `GET /api/slots/available?date=YYYY-MM-DD`
- `POST /api/bookings`
- `GET /api/admin/bookings`
- `PATCH /api/admin/bookings/{id}/status` with `{"status":"ACCEPTED"}` or `{"status":"REJECTED"}`
- `POST /api/admin/slots`

Booking creation locks the slot row in a database transaction, checks remaining trip capacity, and calculates the estimate on the server. Request validation rejects invalid trip, area, hours, or mobile values.

## MVP boundary

This is a modular Spring Boot starter, not the complete microservices production architecture in the product specification. OTP/JWT authentication, authorization, customer-scoped booking access, admin security, audit logs, billing statements, WhatsApp, weather, and the Eureka/gateway/config-server services are not implemented yet. Do not expose the demo admin endpoints to the public internet before adding authentication and role checks.
