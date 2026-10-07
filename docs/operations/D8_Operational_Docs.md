# D8: Operational Documentation

## 1. Administrator Guide & Training Plan
- **Prerequisites:** Windows 11 / Server 2022+, JDK 17+, PostgreSQL 14+.
- **Activation:** Run the provided offline-installer.bat for isolated environments.
- **Training Plan:** IT staff should be trained to review standard Spring Boot structured logs and execute basic PostgreSQL database backup commands.

## 2. User Guide (Librarians)
- **Daily Operations:** Scan books and patron smart-cards normally. The RFID middleware silently handles device-neutral commands.
- **Data Migration:** Place legacy .csv files in the upload directory. The system will auto-reconcile duplicates and invalid entries.
- **Offline Mode:** Continue scanning items if the main LMS goes offline; the system automatically queues and retries events.

## 3. API Documentation
- POST /api/migration/upload - Accepts CSV payloads for batch import and reconciliation.
- POST /api/rfid/event - Consumes standard security gate and inventory events.

## 4. Troubleshooting Runbook
- **Incident:** Security gate events are not syncing. 
  - **Diagnostics:** Verify Ethernet connectivity to the middleware IP. 
  - **Resolution:** None required immediately; the application applies automated offline-queuing and will synchronize upon reconnection.
- **Incident:** Migration fails or imports incorrect counts.
  - **Diagnostics:** Check the generated errorLogs in the MigrationResult payload.
  - **Resolution:** The system automatically executes a transactional rollback. Fix the CSV format and re-upload.