CREATE TABLE conversations (
                               id SERIAL PRIMARY KEY,
                               customer_id BIGINT NOT NULL,
                               farmer_id BIGINT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES users(id) ON DELETE CASCADE
);
