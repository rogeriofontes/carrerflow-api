CREATE TABLE submissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id UUID NOT NULL REFERENCES student_profiles(id),
    challenge_id UUID NOT NULL REFERENCES challenges(id),
    situation TEXT NOT NULL,
    task TEXT NOT NULL,
    action TEXT NOT NULL,
    result TEXT NOT NULL,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_submissions_student_id ON submissions(student_id);
CREATE INDEX idx_submissions_challenge_id ON submissions(challenge_id);
