CREATE TABLE legacy_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    barcode VARCHAR(255) UNIQUE,
    isbn VARCHAR(255)
);
