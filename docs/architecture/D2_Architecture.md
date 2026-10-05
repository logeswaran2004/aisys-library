# D2 - Architecture Package

## System Context Diagram
[Staff/Patron UI] ---> (Library Management System)
[RFID Gates/Readers] ---> (Library Management System)
(Library Management System) ---> [Mock ILMS (NCIP/SIP2)]
(Library Management System) ---> [Mock CCTV / Notifications]

## Database / ER Design (MySQL)
* **users** (id, username, password_hash, role_id)
* **members** (id, member_id, name, email, status)
* **items** (id, accession_number, title_id, rfid_tag_id, status)
* **transactions** (id, item_id, member_id, checkout_date, due_date, return_date)
* **gate_events** (id, gate_id, tag_id, timestamp, cctv_image_ref)
* **migration_batches** (id, filename, start_time, total_records, status)

## API Design (REST / JSON)
* \POST /api/v1/migration/upload\ - Accepts multipart file for staging.
* \POST /api/v1/circulation/checkout\ - \{"itemBarcode": "...", "memberId": "..."}\
* \GET /api/v1/inventory/scan\ - Accepts an array of RFID tags, returns shelf status.
* \POST /api/v1/rfid/gate-event\ - Webhook for gate hardware.

## Event Model
Standardized application events using Spring's event publisher:
* \RfidTagDetectedEvent(String tagId, String deviceId, Instant timestamp)\
* \SecurityGateAlarmEvent(String tagId, String gateId)\
* \MigrationCompletedEvent(Long batchId, int successCount, int errorCount)\

## Mock Contracts (Interoperability Boundaries)
All hardware and external integrations rely on these Java interfaces.

\\\java
public interface LibrarySystemAdapter {
    Item getItem(String accessionNumber);
    Member getMember(String memberId);
    CirculationResult checkout(String itemId, String memberId);
}

public interface RfidReader {
    void connect();
    List<String> readTags();
    boolean writeTag(String tagId, String data);
}

public interface NotificationProvider {
    void sendEmail(String to, String subject, String body);
}
\\\

## Deployment Topology
* **Target OS:** Windows 11 / Windows Server 2022.
* **Environment:** Completely isolated offline LAN.
* **Containers:** Docker Compose running \ackend-app\, \rontend-app\, and \mysql-db\.
