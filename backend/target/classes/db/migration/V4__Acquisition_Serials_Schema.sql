CREATE TABLE acquisitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    vendor VARCHAR(255),
    po_number VARCHAR(100),
    cost DECIMAL(10, 2),
    status VARCHAR(50) DEFAULT 'ORDERED'
);

CREATE TABLE serials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    issn VARCHAR(20),
    frequency VARCHAR(50)
);