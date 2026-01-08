CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY, -- Long ile uyumlu
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) DEFAULT 'ROLE_USER'
);

CREATE TABLE notes (
                       id BIGSERIAL PRIMARY KEY, -- Long ile uyumlu
                       content TEXT NOT NULL,
                       user_id BIGINT NOT NULL, -- User ID de BigInt olmalı
                       CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);