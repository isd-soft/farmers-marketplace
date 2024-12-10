CREATE TABLE product_reviews
(
    id           SERIAL PRIMARY KEY,
    content      VARCHAR(255),
    rating       INTEGER,
    creator_id   BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_creator FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);