CREATE TABLE companies (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID                                        NOT NULL REFERENCES users(id),
    name                VARCHAR(255)                                NOT NULL,
    segment             VARCHAR(255)                                NOT NULL,
    description TEXT,
    website             VARCHAR(500),
    create_by           VARCHAR(255)                                NOT NULL,
    created_date        TIMESTAMP(6)                                NOT NULL,
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP(6),
    status              VARCHAR(255)                                NOT NULL
);

CREATE INDEX idx_companies_user_id ON companies(user_id);
