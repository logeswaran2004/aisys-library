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
