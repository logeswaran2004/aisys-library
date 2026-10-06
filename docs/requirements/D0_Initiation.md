# D0 - Initiation & Controls

## Assumptions
* The offline deployment requires all static assets, container images, and database initialization scripts to be bundled into a single distributable archive.
* The frontend will be served by the Spring Boot embedded Tomcat or a tightly coupled Nginx container.

## Risks
* Simulating realistic asynchronous hardware events (e.g., RFID rapid tag reads) requires a robust concurrency model in the mock implementations.
* Data migration reconciliation could hit memory limits if the 20,000 records are parsed poorly; chunked batch processing is required.

## Dependencies
* Docker ecosystem and Java 21 runtime.
* All third-party libraries will be audited for offline-use restrictions and pinned.

## Excluded Work
* Manufacture, physical installation, or repair of hardware.
* Access to live production systems, production databases, or restricted networks.
* Claims of official vendor certification without test evidence.

## Initial Requirements Traceability Matrix (RTM)
| ID | Requirement | Target Module | Status |
| :--- | :--- | :--- | :--- |
| FR01 | Core ILMS operations | Catalog, Circulation, Members, Acq, Serials | Implemented (D4) |
| FR02 | Web interface & Search | OPAC, Search API | Implemented (D4) |
| FR03 | RFID Interoperability & NCIP/SIP2 | RFID Middleware, ILMS Adapter | Pending |
| FR04 | Circulation & Restrictions | Circulation, Fines, Members | Implemented (D4) |
| FR05 | Tagging & Validation | RFID, Catalog | Pending |
| FR06 | Inventory & Shelf Mgmt | Inventory, RFID | Pending |
| FR07 | Security Gate Events | RFID, Notifications | Pending |
| FR08 | Dashboard & Reports | Reporting, Dashboard API | Implemented (D4) |
| FR10 | 20k Record Migration | Migration, Validation | Pending |
| NFR01| Data Integrity (No overwrites) | DB Constraints, Migration logic| Pending |
| NFR02| Security, Privacy, RBAC | Auth, Spring Security | Pending |
| NFR06| Versioned DB & Modular code | Flyway, Project Structure | Implemented (D3) |

## D4/D5 Architectural Mappings
* **FR01, FR04:** Implemented via CirculationService, MemberService, FineService, AcquisitionRepository, and SerialRepository.
* **FR02:** Implemented via SearchController (OPAC).
* **FR08:** Implemented via DashboardController and ReportController.