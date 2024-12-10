CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        order_status VARCHAR(255) NOT NULL,
                        customer_id BIGINT NOT NULL,
                        farmer_id BIGINT NOT NULL,
                        delivery_type_id BIGINT NOT NULL,
                        total_delivery_price DECIMAL(10, 2) NOT NULL,
                        total_items_price DECIMAL(10, 2) NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL,
                        created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
                        CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES users(id) ON DELETE CASCADE,
                        CONSTRAINT fk_delivery_type FOREIGN KEY (delivery_type_id) REFERENCES delivery_type_farmer(id) ON DELETE CASCADE
);