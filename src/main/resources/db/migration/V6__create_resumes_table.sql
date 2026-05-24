CREATE TABLE resumes (
   id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id             UUID                                      NOT NULL,
   title               VARCHAR(180)                              NOT NULL,
   content_url         VARCHAR(500)                              NOT NULL,
   create_by           VARCHAR(255)                              NOT NULL,
   created_date        TIMESTAMP(6)                              NOT NULL,
   last_modified_by    VARCHAR(255),
   last_modified_date  TIMESTAMP(6),
   status              VARCHAR(255)                              NOT NULL,
   CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_resumes_user ON resumes (user_id);