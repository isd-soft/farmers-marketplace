CREATE TABLE items_in_order (
                                id SERIAL PRIMARY KEY,
                                order_id BIGINT NOT NULL,
                                product_id BIGINT NOT NULL,
                                price_per_unit DECIMAL(10, 2) NOT NULL,
                                quantity INT NOT NULL,
                                FOREIGN KEY (order_id) REFERENCES orders(id),
                                FOREIGN KEY (product_id) REFERENCES products(id)
);
