# D2 - Design Review & Decisions Record

## Date: 2026-10-05
## Participants: Senior Architect, Lead Developer

## Decisions Recorded
1. **Architecture Style:** Approved Modular Monolith (Spring Boot) over Microservices to satisfy the strict offline/isolated deployment requirements.
2. **Database:** Approved MySQL with Flyway migrations to strictly satisfy data integrity requirements and ensure existing records are not silently modified or corrupted.
3. **Hardware Mocks:** Approved the use of Java Interfaces for RfidReader and LibrarySystemAdapter to satisfy the SOP's requirement for unavailable physical hardware and ILMS.
4. **Migration Strategy:** Approved a staging-table approach for the 20,000 spreadsheet records to allow dry-runs, duplicate detection, and rollback before committing to the main production tables.

**Status:** APPROVED. Proceed to D3 Foundation Build.
