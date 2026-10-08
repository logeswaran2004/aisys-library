CREATE TABLE security_gate_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gate_id VARCHAR(255),
    rfid_tag_id VARCHAR(255),
    accession_number VARCHAR(255),
    timestamp TIMESTAMP,
    status VARCHAR(255),
    cctv_image_ref VARCHAR(255)
);
