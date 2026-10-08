CREATE TABLE migration_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255),
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_records INT,
    valid_records INT,
    duplicate_records INT,
    invalid_records INT,
    status VARCHAR(50)
);

UPDATE items SET rfid_tag_id = 'RFID-TAG-1001' WHERE accession_number = 'ACC-1001';

INSERT INTO bibliographic_records (isbn, title, author, publisher, is_reference)
VALUES ('978-0000000001', 'Reference Desk Atlas', 'Library Staff', 'AISYS Press', TRUE);

INSERT INTO items (accession_number, barcode, rfid_tag_id, bibliographic_record_id, status)
SELECT 'ACC-1002', 'BC-1002', 'RFID-TAG-1002', id, 'AVAILABLE'
FROM bibliographic_records WHERE isbn = '978-0000000001';

INSERT INTO items (accession_number, barcode, rfid_tag_id, bibliographic_record_id, status)
SELECT 'ACC-REF-1', 'BC-REF-1', 'RFID-TAG-REF-1', id, 'AVAILABLE'
FROM bibliographic_records WHERE isbn = '978-0000000001';
