CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(255)                                NOT NULL,
    email               VARCHAR(255)                                NOT NULL UNIQUE,
    password            VARCHAR(255)                                NOT NULL,
    role                VARCHAR(50)                                 NOT NULL,
    active              BOOLEAN                                     NOT NULL DEFAULT true,
    create_by           VARCHAR(255)                                NOT NULL,
    created_date        TIMESTAMP(6)                                NOT NULL,
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP(6),
    status              VARCHAR(255)                                NOT NULL
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
