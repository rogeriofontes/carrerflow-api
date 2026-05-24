-- Seed: Admin user (password: admin123!)
--INSERT INTO users (id, name, email, password, role) VALUES
--('a0000000-0000-0000-0000-000000000001', 'Admin CareerFlow', 'admin@careerflow.com',
--'$2a$10$xn3LI/AjqicFYZFruSwve.zOH5hLxCCjYq9tqKC0rDXQECCfBBLi', 'ADMIN');

-- Seed: Student user (password: student123!)
--INSERT INTO users (id, name, email, password, role) VALUES
--('b0000000-0000-0000-0000-000000000001', 'Maria Silva', 'maria@student.com',
-- '$2a$10$xn3LI/AjqicFYZFruSwve.zOH5hLxCCjYq9tqKC0rDXQECCfBBLi', 'STUDENT');

-- Seed: Company user (password: company123!)
--INSERT INTO users (id, name, email, password, role) VALUES
--('c0000000-0000-0000-0000-000000000001', 'TechCorp', 'rh@techcorp.com',
-- '$2a$10$xn3LI/AjqicFYZFruSwve.zOH5hLxCCjYq9tqKC0rDXQECCfBBLi', 'COMPANY');

-- Seed: Institution user (password: inst123!)
--INSERT INTO users (id, name, email, password, role) VALUES
--('d0000000-0000-0000-0000-000000000001', 'Universidade XYZ', 'coord@unixyz.edu.br',
--'$2a$10$xn3LI/AjqicFYZFruSwve.zOH5hLxCCjYq9tqKC0rDXQECCfBBLi', 'INSTITUTION');

-- Seed: Student Profile
/*INSERT INTO student_profiles (id, user_id, course, institution, overall_score, challenges_completed) VALUES
('e0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001',
 'Engenharia de Software', 'Universidade XYZ', 0.0, 0);

INSERT INTO student_skills (student_profile_id, skill) VALUES
('e0000000-0000-0000-0000-000000000001', 'Java'),
('e0000000-0000-0000-0000-000000000001', 'Spring Boot'),
('e0000000-0000-0000-0000-000000000001', 'PostgreSQL'),
('e0000000-0000-0000-0000-000000000001', 'Docker');

-- Seed: Company Profile
INSERT INTO companies (id, user_id, name, segment, description, website) VALUES
('f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001',
 'TechCorp Solutions', 'Technology', 'Enterprise software development', 'https://techcorp.com');

-- Seed: Challenges
INSERT INTO challenges (id, title, description, difficulty, company_id) VALUES
('10000000-0000-0000-0000-000000000001',
 'Desenvolvimento de API REST',
 'Desenvolva uma API REST completa para gerenciamento de tarefas, incluindo autenticação JWT, CRUD de tarefas, e documentação com Swagger.',
 'MEDIUM', 'f0000000-0000-0000-0000-000000000001'),
('10000000-0000-0000-0000-000000000002',
 'Otimização de Banco de Dados',
 'Analise e otimize queries SQL em um sistema com milhões de registros. Identifique gargalos e implemente melhorias de performance.',
 'HARD', 'f0000000-0000-0000-0000-000000000001'),
('10000000-0000-0000-0000-000000000003',
 'Implementação de Microsserviços',
 'Quebre um monolito em microsserviços usando Spring Boot, Docker e Kubernetes. Implemente comunicação assíncrona com mensageria.',
 'EXPERT', 'f0000000-0000-0000-0000-000000000001');

INSERT INTO challenge_skills (challenge_id, skill) VALUES
('10000000-0000-0000-0000-000000000001', 'Java'),
('10000000-0000-0000-0000-000000000001', 'Spring Boot'),
('10000000-0000-0000-0000-000000000001', 'REST'),
('10000000-0000-0000-0000-000000000002', 'SQL'),
('10000000-0000-0000-0000-000000000002', 'PostgreSQL'),
('10000000-0000-0000-0000-000000000002', 'Performance'),
('10000000-0000-0000-0000-000000000003', 'Microsserviços'),
('10000000-0000-0000-0000-000000000003', 'Docker'),
('10000000-0000-0000-0000-000000000003', 'Kubernetes');
*/