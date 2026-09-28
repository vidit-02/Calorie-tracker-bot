# Calorie-AI Backend

Spring Boot REST backend for the Calorie-AI project: foods, meals, daily logs, user profile, and an AI confirmation workflow (pending actions are held in memory until `/api/confirm`).

## Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 16+ (or Docker)

## Database

Create database `calorie_ai` or start Postgres via Docker Compose:

```bash
docker compose up -d db
```

Default credentials (see `application.properties`):

- URL: `jdbc:postgresql://localhost:5432/calorie_ai`
- User / password: `calorie` / `calorie`

Flyway runs `src/main/resources/db/migration/V1__init.sql` on startup.

## Run locally

```bash
./mvnw spring-boot:run
```

API base: `http://localhost:8080`

Main endpoints:

- `POST /api/ai` — AI JSON intents (creates pending action)
- `POST /api/confirm` — confirm or cancel pending action
- `GET,POST /api/foods`, `PUT,DELETE /api/foods/{id}`
- `GET,POST /api/meals`, `PUT,DELETE /api/meals/{id}`
- `GET /api/logs?date=YYYY-MM-DD`, `DELETE /api/logs/{id}`
- `GET,PUT /api/profile`

## Build and test

```bash
./mvnw clean package
./mvnw test
```

## Docker (app + Postgres)

```bash
./mvnw clean package -DskipTests
docker compose up --build
```

Production DB settings can be overridden with:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
