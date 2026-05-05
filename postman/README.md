# Postman collection — MediaOps API

Importable Postman collection covering every endpoint in this repo.

## Quick import

1. Postman → **File → Import** → select `mediaops.postman_collection.json`.
2. The collection appears in the left sidebar with three folders: **Health**, **Videos**, **Plays**.
3. Make sure the Spring Boot app is running locally (`./mvnw spring-boot:run` from repo root, port `8081`).
4. Click any request → **Send**.

## Variables

The collection has one variable, `baseUrl`, defaulted to `http://localhost:8081`. To switch environments:

- Click the collection name → **Variables** tab → edit `baseUrl`.
- Or create a Postman **environment** (e.g., "staging", "prod") and override `baseUrl` there. The collection picks up the active environment's value automatically.

## What's in it

| Folder | Request | What it demonstrates |
|---|---|---|
| Health | `GET /actuator/health` | Spring Boot Actuator liveness probe |
| Videos | `GET /api/v2/videos` | Repository `findAll` + DTO mapping |
| Videos | `GET /api/v2/videos?category=Animation` | Spring Data derived query (`findByCategoryOrderByCreatedAtDesc`) |
| Videos | `GET /api/v2/videos/{id}` | PK lookup; 404 path via `@RestControllerAdvice` |
| Plays | `POST /api/v2/plays` | Write endpoint, transaction, dirty-check view-count update |
| Plays | `POST /api/v2/plays` (invalid) | Bean Validation (`@NotNull`) → 400 with per-field error array |

Each request has a saved example response so you can preview the contract without firing it.
