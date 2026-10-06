# API Catalogue (Stage 4 Completed)

## Members (FR01)
* **GET /api/v1/members**
  * **Purpose:** Lists all members.
* **GET /api/v1/members/{memberId}**
  * **Purpose:** Retrieves specific member details (including fine balance).
* **POST /api/v1/members**
  * **Purpose:** Creates a new library member.

## Catalog & Search (FR01, FR02)
* **GET /api/v1/catalog/records**
  * **Purpose:** Lists all bibliographic records.
* **POST /api/v1/catalog/records**
  * **Purpose:** Creates a new bibliographic record.
* **GET /api/v1/search?query={text}**
  * **Purpose:** Full-text search for OPAC (Virtual Bookshelf) matching Title or Author.

## Circulation (FR01, FR04)
* **POST /api/v1/circulation/checkout**
  * **Purpose:** Checks out an item to a member. Enforces fine limits, reference restrictions, and blocked status.
  * **Auth:** Requires LIBRARIAN or CIRCULATION_STAFF role.

## Fines Management (FR04)
* **POST /api/v1/fines/{memberId}/pay?amount={amount}**
  * **Purpose:** Processes a fine payment and reduces the member's fine balance.

## Dashboard & Reporting (FR08)
* **GET /api/v1/dashboard/stats**
  * **Purpose:** Retrieves library usage statistics (total items, members).
* **GET /api/v1/reports/circulation?status={status}**
  * **Purpose:** Generates a filterable circulation report (e.g., status=OVERDUE).

## Acquisitions & Serials (FR01)
* **GET /api/v1/acquisitions** (Stubbed via Repository)
* **GET /api/v1/serials** (Stubbed via Repository)