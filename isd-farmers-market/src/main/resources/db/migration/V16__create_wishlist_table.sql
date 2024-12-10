CREATE TABLE wishlist (
                          user_id BIGINT NOT NULL,
                          product_id BIGINT NOT NULL,
                          PRIMARY KEY (user_id, product_id),
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (product_id) REFERENCES products(id)
);