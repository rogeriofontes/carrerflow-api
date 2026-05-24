CREATE TABLE job_applications (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID                                        NOT NULL,
    company_id          UUID                                        NOT NULL,
    position            VARCHAR(180)                                NOT NULL,
    application_status  VARCHAR(30)                                 NOT NULL,
    applied_at          DATE                                        NOT NULL,
    notes               VARCHAR(2000),
    create_by           VARCHAR(255)                                NOT NULL,
    created_date        TIMESTAMP(6)                                NOT NULL,
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP(6),
    status              VARCHAR(255)                                NOT null,
    CONSTRAINT fk_job_applications_user    FOREIGN KEY (user_id)    REFERENCES users (id),
    CONSTRAINT fk_job_applications_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX idx_job_applications_user    ON job_applications (user_id);
CREATE INDEX idx_job_applications_company ON job_applications (company_id);
CREATE INDEX idx_job_applications_status  ON job_applications (status);