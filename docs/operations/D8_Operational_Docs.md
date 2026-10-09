# D8: Operational Documentation

## 1. Administrator Guide & Training Plan
- **Prerequisites:** Windows 11 / Server 2022+, JDK 21+, H2 (default, MySQL-compatible) or MySQL 8+.
- **Activation:** Run the provided offline-installer.bat for isolated environments.
- **Training Plan:** IT staff should be trained to review standard Spring Boot structured logs and execute basic H2 / MySQL 8 database backup commands.

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

## 5. Offline Update and Rollback Procedure (AC 09)

**Applying an Offline Update:**
1. Transfer the offline update package (`update-package.jar`) to the isolated server via secure USB.
2. Place the package in the `deployment/update/` directory.
3. Stop the running Spring Boot application.
4. Execute `deployment/update/apply-update.bat`. This script automatically backs up the current active version to the `rollback` directory before applying the new patch.
5. Restart the application.

**Executing a Rollback:**
If the update fails or introduces critical defects, follow these steps to restore the system:
1. Stop the running Spring Boot application.
2. Navigate to the `deployment/rollback/` directory.
3. Execute `rollback-system.bat`. This will overwrite the corrupted/updated application binary with the stable `aisys-library-backend-PREVIOUS.jar`.
4. Run the AC 10 Database Restore process if database schema changes need to be reverted.
5. Restart the application.