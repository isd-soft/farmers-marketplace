CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       role VARCHAR(50) NOT NULL
);

INSERT INTO roles (role) VALUES
                             ('ADMIN'),
                             ('FARMER'),
                             ('CUSTOMER');