CREATE TABLE delivery_type_farmer (
                                      id SERIAL PRIMARY KEY,
                                      price DECIMAL(10, 2) NOT NULL,
                                      type SMALLINT NOT NULL,
                                      farmer_id BIGINT NOT NULL,
                                      CONSTRAINT fk_farmer FOREIGN KEY (farmer_id) REFERENCES users(id)
);
