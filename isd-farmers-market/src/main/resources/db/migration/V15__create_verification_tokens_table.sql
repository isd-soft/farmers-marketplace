CREATE TABLE verification_tokens (
                                     id SERIAL PRIMARY KEY,
                                     token VARCHAR(255) UNIQUE NOT NULL,
                                     user_id BIGINT NOT NULL,
                                     expiry_date TIMESTAMP NOT NULL,
                                     CONSTRAINT fk_verification_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
