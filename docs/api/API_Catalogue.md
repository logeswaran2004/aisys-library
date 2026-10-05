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