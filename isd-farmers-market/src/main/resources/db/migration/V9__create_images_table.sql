CREATE TABLE images (
                        id SERIAL PRIMARY KEY,
                        bytes OID NOT NULL,
                        product_id BIGINT,
                        CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
