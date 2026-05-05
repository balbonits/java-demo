# java-demo — MediaOps API

Spring Boot REST service over Postgres, modelling a small slice of a content-pipeline domain (videos, plays, availability windows, asset state). Built to demonstrate the JPA / Spring Data / `@Transactional` / DTO / `@RestControllerAdvice` patterns end-to-end.

The Postgres database is shared with [`postgresql-demo`](https://github.com/balbonits/postgresql-demo) — a separate Next.js + `postgres.js` app that reads/writes the same tables. **One database, two backends.**

## Stack

- Java 17, Spring Boot 3.5, Maven (`./mvnw`)
- Spring Web, Spring Data JPA, Hibernate 6
- Bean Validation (Jakarta), Spring Boot Actuator
- PostgreSQL 16 (Docker container, shared with the sibling Next.js project)

## Architecture

```
                              ┌── Next.js routes (postgresql-demo, port 3000)
                              │   /api/videos, /api/plays, /api/analytics/*
                              │
[ video_catalog Postgres :5432 ]
   videos, plays                ─── pre-existing
   availability_windows          ─── added by Spring (ddl-auto=update)
   asset_states
                              │
                              └── Spring Boot REST API (this repo, port 8081)
                                  /api/v2/videos, /api/v2/plays, /actuator/*
```

```
src/main/java/com/example/mediaops/
├── api/                              # HTTP layer
│   ├── VideoController.java          # GET /api/v2/videos, /{id}
│   ├── PlayController.java           # POST /api/v2/plays
│   ├── ApiExceptionHandler.java      # @RestControllerAdvice -> JSON errors
│   ├── VideoDto.java                 # wire-format record
│   └── RecordPlayRequest.java        # validated request body
├── service/
│   └── CatalogService.java           # @Service, @Transactional business logic
└── domain/
    ├── Video.java                    # @Entity -> videos
    ├── Play.java                     # @Entity -> plays  (FK to Video)
    ├── AvailabilityWindow.java       # @Entity -> availability_windows
    ├── AssetState.java               # @Entity -> asset_states + state machine
    ├── VideoRepository.java          # Spring Data JPA — derived queries
    ├── PlayRepository.java
    └── AssetStateRepository.java
```

## Running locally

Prereqs: Java 17, Docker (for the Postgres container).

```bash
# 1. Make sure the Postgres container from postgresql-demo is running
docker ps | grep video-catalog-db
# If not: docker run --name video-catalog-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=video_catalog -p 5432:5432 -d postgres

# 2. From the project root
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
./mvnw spring-boot:run
```

App listens on `http://localhost:8081`. On first boot, Hibernate adds the `availability_windows` and `asset_states` tables to the existing schema (`ddl-auto=update`).

## Endpoints

| Method | Path | Notes |
|---|---|---|
| `GET` | `/actuator/health` | Spring Boot Actuator probe |
| `GET` | `/api/v2/videos` | List all videos |
| `GET` | `/api/v2/videos?category={cat}` | Filter by category |
| `GET` | `/api/v2/videos/{id}` | Single video; 404 with structured error if missing |
| `POST` | `/api/v2/plays` | Records a play; transactional view-count increment via Hibernate dirty checking |

A Postman collection covering every endpoint is in [`postman/`](postman/).

## Domain notes

- **Video, Play** map to tables that already exist in `video_catalog` (created by the sibling Next.js app).
- **AvailabilityWindow** models the "when/where this video is available" concept (region, start/end timestamps).
- **AssetState** encapsulates a state machine for the underlying media asset:
  ```
  INGESTING -> ENCODING -> READY
  *         -> FAILED  (terminal)
  ```
  Transitions are validated inside the entity (`AssetState.transitionTo(...)`); illegal transitions throw `IllegalStateException`, which `ApiExceptionHandler` maps to HTTP 409.

## What this demonstrates

- JPA / Hibernate mapping (entities, relationships, naming strategies, indexes, FK constraints, enum-as-string with check constraints)
- Spring Data JPA derived queries (`findByCategoryOrderByCreatedAtDesc`) — no hand-written SQL
- Layered architecture (controller → service → repository → entity)
- Constructor injection, `@Transactional` boundaries, `readOnly` optimization
- Bean Validation at the API boundary
- Centralized error mapping via `@RestControllerAdvice`
- Java records for immutable wire-format DTOs
- Dirty-check-driven UPDATEs (the play increment never writes an explicit UPDATE statement)

## What's intentionally NOT here (would be the next iteration)

- Flyway/Liquibase migrations (currently using `ddl-auto=update` for dev convenience)
- Pagination on list endpoints (`Pageable` parameter)
- A second-language NoSQL tier (Cassandra/Mongo) for high-throughput event logs
- Kafka event emission on title transitions
- Testcontainers integration tests
- CI pipeline + container image
- Auth (Spring Security + OAuth2 / JWT)

These belong in the design conversation rather than this proof-of-concept.

## License

MIT — see `LICENSE`.
