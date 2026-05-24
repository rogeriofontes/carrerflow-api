CREATE TABLE challenges (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title                VARCHAR(255)                                NOT NULL,
    description          TEXT                                        NOT NULL,
    difficulty           VARCHAR(50)                                 NOT NULL,
    company_id           UUID REFERENCES companies(id),
    active               BOOLEAN                                     NOT NULL DEFAULT true,
    create_by            VARCHAR(255)                                NOT NULL,
    created_date         TIMESTAMP(6)                                NOT NULL,
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMP(6),
    status               VARCHAR(255)                                NOT NULL
);

CREATE TABLE challenge_skills (
    challenge_id UUID NOT NULL REFERENCES challenges(id),
    skill VARCHAR(255) NOT NULL
);

CREATE INDEX idx_challenges_difficulty ON challenges(difficulty);
CREATE INDEX idx_challenges_company_id ON challenges(company_id);
CREATE INDEX idx_challenges_active ON challenges(active);
CREATE INDEX idx_challenge_skills_skill ON challenge_skills(skill);
