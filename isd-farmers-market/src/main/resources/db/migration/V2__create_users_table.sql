CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       enabled BOOLEAN NOT NULL DEFAULT false,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       phone_number VARCHAR(50),
                       description TEXT,
                       review_count INTEGER,
                       address VARCHAR(255),
                       password VARCHAR(1000) NOT NULL,
                       free_delivery_from INTEGER DEFAULT 0,
                       rating FLOAT DEFAULT 0,
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
