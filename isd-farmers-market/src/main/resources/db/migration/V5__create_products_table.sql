CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          title VARCHAR(80) NOT NULL,
                          description VARCHAR(1000),
                          unit_type SMALLINT,
                          price_per_unit DECIMAL(10, 2) NOT NULL,
                          discount_percents INTEGER DEFAULT 0,
                          quantity INT NOT NULL,
                          visible BOOLEAN NOT NULL DEFAULT TRUE,
                          category_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          rating FLOAT,
                          review_count INTEGER DEFAULT 0,
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
