# D1 - Requirements Package

## Use Cases
1. **UC01 - Migrate Records:** Admin uploads a spreadsheet of 20k records; system profiles, stages, validates, rejects duplicates, migrates, and provides a reconciliation report.
2. **UC02 - RFID Tagging:** Staff scans an item barcode, system retrieves bibliographic record, staff reads a blank RFID tag, system associates tag ID with the item.
3. **UC03 - Circulation (Mock ILMS):** Staff checks out an item; system communicates with Mock ILMS via NCIP to verify member standing and check out the book.
4. **UC04 - Inventory Scan:** Staff uses a handheld RFID reader; system identifies missing, expected, and misplaced items on a shelf, providing audible feedback.
5. **UC05 - Security Gate Alarm:** Gate detects an unissued tag; system logs event, requests mock CCTV image, and queues an email notification.

## Data Dictionary
* **Member:** Patron details (ID, Name, Type, Status).
* **BibliographicRecord:** Title, Author, ISBN, Publisher.
* **Item:** Specific physical copy of a book (Accession Number, Barcode, RFID Tag, Status).
* **RfidTag:** Physical hardware tag associated with an Item (TagID, EPC, Status).
* **CirculationTransaction:** Record of checkout/checkin/renewal (TransactionID, ItemID, MemberID, DueDate).
* **SecurityGateEvent:** Record of gate alarms (EventID, GateID, TagID, ImagePath, Timestamp).
* **MigrationStaging:** Temporary holding for spreadsheet rows before validation.

## Acceptance Criteria
* **AC01 - AC10:** Defined per the SOP priority acceptance scenarios. See root README/RTM for tracking.

## Assumptions
1. **Infrastructure:** Target deployment environment is an isolated Windows Server 2022 or Windows 11 client with no internet access.
2. **Data Availability:** Legacy migration data is provided as a clean CSV matching the 20,000-record threshold constraint.
3. **Hardware Integration:** Physical RFID gates, handheld scanners, and smart card readers are unavailable; interfaces are fulfilled via mocked software adapters.

## Clarification Log
- **Q:** How should offline deployment be handled without external package managers?
  - **A:** The system is packaged as a self-contained Spring Boot executable JAR (fat JAR) with all Maven dependencies bundled.
- **Q:** What is the mechanism for the AC 10 backup requirement?
  - **A:** An embedded backup service handles active file-copy snapshots of the local H2 `.mv.db` database file before data state changes.

## Traceability Matrix
*(Note: Full Requirement-to-Feature mapping is tracked in docs/verification/Test_Results_RTM.md)*