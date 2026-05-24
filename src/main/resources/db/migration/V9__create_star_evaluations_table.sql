CREATE TABLE star_evaluations (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id       UUID                                            NOT NULL UNIQUE REFERENCES submissions(id),
    situation_score     DOUBLE PRECISION                                NOT NULL,
    task_score          DOUBLE PRECISION                                NOT NULL,
    action_score        DOUBLE PRECISION                                NOT NULL,
    result_score        DOUBLE PRECISION                                NOT NULL,
    final_score         DOUBLE PRECISION                                NOT NULL,
    feedback            TEXT                                            NOT NULL,
    evaluated_at        TIMESTAMP                                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    create_by           VARCHAR(255)                                    NOT NULL,
    created_date        TIMESTAMP(6)                                    NOT NULL,
    last_modified_by    VARCHAR(255),
    last_modified_date  TIMESTAMP(6),
    status              VARCHAR(255)                                    NOT NULL
);

CREATE INDEX idx_star_evaluations_submission_id ON star_evaluations(submission_id);
CREATE INDEX idx_star_evaluations_final_score ON star_evaluations(final_score);
