CREATE TABLE student_profiles (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id              UUID NOT NULL UNIQUE REFERENCES users(id),
    course               VARCHAR(255)                               NOT NULL,
    institution          VARCHAR(255)                               NOT NULL,
    overall_score        DOUBLE PRECISION DEFAULT 0.0,
    challenges_completed INTEGER DEFAULT 0,
    create_by            VARCHAR(255)                                NOT NULL,
    created_date         TIMESTAMP(6)                                NOT NULL,
    last_modified_by     VARCHAR(255),
    last_modified_date   TIMESTAMP(6),
    status               VARCHAR(255)                                NOT NULL
);

CREATE TABLE student_skills (
    student_profile_id UUID NOT NULL REFERENCES student_profiles(id),
    skill VARCHAR(255) NOT NULL
);

CREATE INDEX idx_student_profiles_user_id ON student_profiles(user_id);
CREATE INDEX idx_student_profiles_score ON student_profiles(overall_score);
CREATE INDEX idx_student_skills_skill ON student_skills(skill);
