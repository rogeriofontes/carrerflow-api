CREATE TABLE interviews (
   id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   job_application_id  UUID                                         NOT NULL,
   type                VARCHAR(30)                                  NOT NULL,
   scheduled_at        TIMESTAMP                                    NOT NULL,
   location            VARCHAR(255),
   result              VARCHAR(30)                                 NOT NULL,
   notes               VARCHAR(2000),
   create_by           VARCHAR(255)                                NOT NULL,
   created_date        TIMESTAMP(6)                                NOT NULL,
   last_modified_by    VARCHAR(255),
   last_modified_date  TIMESTAMP(6),
   status              VARCHAR(255)                                NOT NULL,
   CONSTRAINT fk_interviews_job_application FOREIGN KEY (job_application_id) REFERENCES job_applications (id)
);

CREATE INDEX idx_interviews_job_application ON interviews (job_application_id);