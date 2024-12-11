CREATE TABLE refresh_tokens (
                                id SERIAL PRIMARY KEY,
                                token VARCHAR(255) NOT NULL,
                                user_id BIGINT NOT NULL,
                                CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
