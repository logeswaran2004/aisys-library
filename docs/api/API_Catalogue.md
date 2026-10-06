# API Catalogue (D4 Update)

## Circulation
* **POST /api/v1/circulation/checkout**
  * **Purpose:** Checks out an item to a member. Enforces fine limits, reference restrictions, and blocked status.
  * **Auth:** Requires LIBRARIAN or CIRCULATION_STAFF role.

## Catalog & Search (OPAC)
* **GET /api/v1/catalog/records**
  * **Purpose:** Lists all bibliographic records.
* **GET /api/v1/search?query={text}**
  * **Purpose:** Full-text search for OPAC (Virtual Bookshelf) matching Title or Author.

## Dashboard
* **GET /api/v1/dashboard/stats**
  * **Purpose:** Retrieves library usage statistics (total items, members, tagged items).
## Acquisitions & Serials (FR01)
* **GET /api/v1/acquisitions** (Stubbed via Repository)
* **GET /api/v1/serials** (Stubbed via Repository)

## Filterable Reports (FR08)
* **GET /api/v1/reports/circulation?status={status}**
  * **Purpose:** Generates a filterable circulation report (e.g., status=OVERDUE).

## Fines Management (FR04)
* **POST /api/v1/fines/{memberId}/pay?amount={amount}**
  * **Purpose:** Processes a fine payment and reduces the member's fine balance.
  * **Auth:** Requires LIBRARIAN or CIRCULATION_STAFF role.
