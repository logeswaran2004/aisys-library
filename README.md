# AISYS RFID-Enabled Library Management System

Production-oriented, offline-capable library management extension that acts as RFID middleware and an interoperability layer for catalog, circulation, inventory, migration, and security-gate workflows.

**Data privacy:** All member records, API keys, passwords, and credentials in this repository are synthetic. No live institutional data is used.

## Stack

- Backend: Spring Boot 3.2 / Java 21 (modular monolith)
- Database: H2 (default local/dev) or MySQL 8 (Docker / `mysql` profile)
- Schema: Flyway migrations under `backend/src/main/resources/db/migration`
- UI: static operator desk served from the backend at `/` (also proxied by `frontend-app` on port 3000)
- Auth: JWT (`Authorization: Bearer …`) plus HTTP Basic for admin tools such as the H2 console

## Default demo accounts

| Username   | Password       | Role               |
|------------|----------------|--------------------|
| admin      | admin123       | ADMIN              |
| librarian  | librarian123   | LIBRARIAN          |
| staff      | staff123       | CIRCULATION_STAFF  |
| inventory  | inventory123   | INVENTORY_STAFF    |
| viewer     | viewer123      | VIEWER (read-only) |

Seed members: `M1001` (active), `M1002` (blocked). Seed barcodes: `BC-1001`, `BC-1002`, `BC-REF-1`.

## Build and run (local H2)

```bash
cd backend
mvn spring-boot:run
```

Open http://localhost:8080/ and log in. API base path is `/api/v1`.

The H2 console is at `/h2-console` and requires **ADMIN** credentials (HTTP Basic: `admin` / `admin123`). JDBC URL: `jdbc:h2:file:./data/aisys-library;MODE=MySQL;AUTO_SERVER=TRUE`.

## MySQL profile

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Defaults (override with env vars `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`):

- URL: `jdbc:mysql://localhost:3306/aisys_library`
- User / password: `aisys` / `aisys`

## Docker Compose (D2 layout)

```bash
docker compose up --build
```

Services: `mysql-db` (3306), `backend-app` (8080), `frontend-app` (3000 → proxies the backend UI/API).

## Tests

Automated tests live in `backend/src/test` (the top-level `tests/` folders are markers that point here).

```bash
cd backend
mvn test
```

Useful slices:

- `com.aisys.library.CirculationJourneyTest` — checkout rules
- `com.aisys.library.verification.SecurityVerificationTest` — Spring Security / JWT RBAC via MockMvc
- `com.aisys.library.verification.PerformanceVerificationTest` — `InventoryService.reconcileShelf` and 20k-row migration dry-run

## Selected API surface

- Auth: `POST /api/v1/auth/login`
- Users/roles (ADMIN): `/api/v1/users`, `/api/v1/roles` (GET/POST/PUT/DELETE)
- Catalog: `/api/v1/catalog/records`, `/api/v1/catalog/items` (GET list/by-id, POST, PUT, DELETE)
- Circulation: `POST /checkout`, `POST /checkin`, `POST /renew`, `GET /`, `GET /{id}`, `GET /member/{memberId}`
- Fines: `GET /api/v1/fines`, `GET /api/v1/fines/{memberId}`, `POST /api/v1/fines/{memberId}`, `POST /api/v1/fines/{memberId}/pay`
- Audit: `GET /api/v1/audit?actor=&action=&resource=`
- Migration: `POST /api/v1/migration/upload` (sends completion email via mock provider)

CSV migration columns: `Title,Author,Barcode,ISBN`.

## SBOM

CycloneDX inventory: `SBOM/cyclonedx-sbom.json`.
