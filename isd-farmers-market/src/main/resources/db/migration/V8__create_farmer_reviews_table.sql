CREATE TABLE farmer_reviews (
                                id SERIAL PRIMARY KEY,
                                content VARCHAR(255) NOT NULL,
                                rating FLOAT NOT NULL,
                                creator_id BIGINT NOT NULL,
                                farmer_id BIGINT NOT NULL,
                                created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
                                CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES users(id) ON DELETE CASCADE
);
