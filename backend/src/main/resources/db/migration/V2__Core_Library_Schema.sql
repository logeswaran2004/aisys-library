CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    fine_balance DECIMAL(10, 2) DEFAULT 0.00
);

CREATE TABLE bibliographic_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    is_reference BOOLEAN DEFAULT FALSE
);

CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accession_number VARCHAR(50) NOT NULL UNIQUE,
    barcode VARCHAR(100) UNIQUE,
    rfid_tag_id VARCHAR(100) UNIQUE,
    bibliographic_record_id BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    FOREIGN KEY (bibliographic_record_id) REFERENCES bibliographic_records(id)
);

CREATE TABLE circulation_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    checkout_date TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Seed Data for Testing Circulation Logic
INSERT INTO members (member_id, name, email, status) VALUES ('M1001', 'John Doe', 'john@example.com', 'ACTIVE');
INSERT INTO members (member_id, name, email, status) VALUES ('M1002', 'Jane Blocked', 'jane@example.com', 'BLOCKED');

INSERT INTO bibliographic_records (isbn, title, author, publisher, is_reference) VALUES ('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', FALSE);
INSERT INTO items (accession_number, barcode, bibliographic_record_id, status) VALUES ('ACC-1001', 'BC-1001', 1, 'AVAILABLE');