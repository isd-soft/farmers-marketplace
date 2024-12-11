CREATE TABLE planned_orders (
                                id SERIAL PRIMARY KEY,
                                customer_id BIGINT NOT NULL,
                                product_id BIGINT NOT NULL,
                                delivery_type_id BIGINT NOT NULL,
                                quantity INTEGER NOT NULL,
                                day_of_week VARCHAR(9),
                                time TIME,
                                active BOOLEAN DEFAULT FALSE,
                                created_date TIMESTAMP,
                                CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                                CONSTRAINT fk_delivery_type FOREIGN KEY (delivery_type_id) REFERENCES delivery_type_farmer(id) ON DELETE CASCADE
);
