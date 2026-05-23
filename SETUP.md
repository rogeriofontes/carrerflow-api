# SETUP.md — Guia Completo para Rodar o CareerFlow API

Este guia cobre todas as formas de configurar e executar o projeto, desde o setup mais rápido (Docker) até o ambiente completo de desenvolvimento local.

---

## Índice

1. [Pré-requisitos](#1-pré-requisitos)
2. [Setup Rápido com Docker Compose](#2-setup-rápido-com-docker-compose)
3. [Setup de Desenvolvimento Local](#3-setup-de-desenvolvimento-local)
4. [Configuração do Banco de Dados](#4-configuração-do-banco-de-dados)
5. [Variáveis de Ambiente](#5-variáveis-de-ambiente)
6. [Executando a Aplicação](#6-executando-a-aplicação)
7. [Executando os Testes](#7-executando-os-testes)
8. [Acessando a Documentação (Swagger)](#8-acessando-a-documentação-swagger)
9. [Configurando a Integração com OpenAI](#9-configurando-a-integração-com-openai)
10. [Testando os Endpoints](#10-testando-os-endpoints)
11. [Troubleshooting](#11-troubleshooting)
12. [Estrutura do Projeto](#12-estrutura-do-projeto)

---

## 1. Pré-requisitos

### Para rodar com Docker (recomendado)

| Ferramenta | Versão Mínima | Verificar instalação |
|---|---|---|
| Docker | 24+ | `docker --version` |
| Docker Compose | 2.20+ | `docker compose version` |

### Para desenvolvimento local

| Ferramenta | Versão Mínima | Verificar instalação |
|---|---|---|
| Java (JDK) | 21 | `java --version` |
| Maven | 3.9+ | `mvn --version` |
| PostgreSQL | 16 | `psql --version` |
| Docker | 24+ | `docker --version` (para banco via container) |

### Instalação do Java 21 (via SDKMAN)

```bash
# Instalar SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Instalar Java 21
sdk install java 21.0.5-tem

# Instalar Maven
sdk install maven 3.9.9

# Verificar
java --version   # openjdk 21.0.5
mvn --version    # Apache Maven 3.9.9
```

### Instalação alternativa (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-21-jdk maven -y
```

### Instalação alternativa (macOS com Homebrew)

```bash
brew install openjdk@21 maven
```

---

## 2. Setup Rápido com Docker Compose

A forma mais rápida de ter tudo funcionando:

```bash
# 1. Clonar o repositório
git clone https://github.com/rogeriofontes/carrerflow-api.git
cd carrerflow-api

# 2. Subir tudo (PostgreSQL + API)
docker compose up -d

# 3. Verificar se os containers estão rodando
docker compose ps

# 4. Acompanhar logs da API
docker compose logs -f api
```

**Aguarde ~30 segundos** para o build do Docker e startup da aplicação.

### Verificar se está funcionando

```bash
# Health check
curl http://localhost:8080/api/v1/actuator/health

# Resposta esperada:
# {"status":"UP"}
```

### URLs disponíveis

| Serviço | URL |
|---|---|
| API Base | http://localhost:8080/api/v1 |
| Swagger UI | http://localhost:8080/api/v1/swagger-ui.html |
| API Docs (JSON) | http://localhost:8080/api/v1/api-docs |
| Actuator Health | http://localhost:8080/api/v1/actuator/health |
| PostgreSQL | localhost:5432 (user: careerflow / pass: careerflow) |

### Parar os serviços

```bash
docker compose down          # Para os containers
docker compose down -v       # Para e remove os volumes (limpa o banco)
```

---

## 3. Setup de Desenvolvimento Local

### 3.1. Subir apenas o PostgreSQL via Docker

```bash
docker compose up -d postgres
```

### 3.2. Criar o arquivo `.env` (opcional)

```bash
cat > .env << 'EOF'
DB_HOST=localhost
DB_PORT=5432
DB_NAME=careerflow
DB_USER=careerflow
DB_PASSWORD=careerflow
JWT_SECRET=Y2FyZWVyZmxvdy1zZWNyZXQta2V5LWZvci1kZXZlbG9wbWVudC1vbmx5LTEyMzQ1Njc4OQ==
OPENAI_API_KEY=
EOF
```

### 3.3. Compilar o projeto

```bash
mvn clean compile
```

### 3.4. Executar a aplicação

```bash
mvn spring-boot:run
```

Ou com variáveis de ambiente:

```bash
DB_HOST=localhost DB_PORT=5432 mvn spring-boot:run
```

### 3.5. Gerar o JAR e executar

```bash
mvn clean package -DskipTests
java -jar target/careerflow-api-0.1.0-SNAPSHOT.jar
```

---

## 4. Configuração do Banco de Dados

### Via Docker (recomendado)

O `docker-compose.yml` já configura o PostgreSQL automaticamente:
- **Banco:** careerflow
- **Usuário:** careerflow
- **Senha:** careerflow
- **Porta:** 5432

### Via PostgreSQL local

Se preferir instalar o PostgreSQL diretamente:

```bash
# Ubuntu
sudo apt install postgresql postgresql-contrib -y
sudo systemctl start postgresql

# Criar banco e usuário
sudo -u postgres psql << 'SQL'
CREATE USER careerflow WITH PASSWORD 'careerflow';
CREATE DATABASE careerflow OWNER careerflow;
GRANT ALL PRIVILEGES ON DATABASE careerflow TO careerflow;
SQL
```

```bash
# macOS (Homebrew)
brew install postgresql@16
brew services start postgresql@16
createuser -s careerflow
createdb -O careerflow careerflow
```

### Flyway Migrations

As migrations são executadas automaticamente ao iniciar a aplicação. Elas criam:

| Migration | Descrição |
|---|---|
| V1 | Tabela `users` |
| V2 | Tabelas `student_profiles` + `student_skills` |
| V3 | Tabela `companies` |
| V4 | Tabelas `challenges` + `challenge_skills` |
| V5 | Tabela `submissions` |
| V6 | Tabela `star_evaluations` |
| V7 | Seed data (usuários, perfis, desafios) |

### Acessar o banco

```bash
# Via Docker
docker exec -it careerflow-db psql -U careerflow -d careerflow

# Via psql local
psql -h localhost -U careerflow -d careerflow
```

```sql
-- Verificar tabelas
\dt

-- Verificar usuários seed
SELECT id, name, email, role FROM users;

-- Verificar desafios
SELECT id, title, difficulty FROM challenges;
```

---

## 5. Variáveis de Ambiente

| Variável | Padrão | Obrigatória | Descrição |
|---|---|---|---|
| `DB_HOST` | `localhost` | Não | Host do PostgreSQL |
| `DB_PORT` | `5432` | Não | Porta do PostgreSQL |
| `DB_NAME` | `careerflow` | Não | Nome do banco |
| `DB_USER` | `careerflow` | Não | Usuário do banco |
| `DB_PASSWORD` | `careerflow` | Não | Senha do banco |
| `JWT_SECRET` | (chave dev) | **Sim em produção** | Chave secreta JWT (Base64, mín. 32 bytes) |
| `OPENAI_API_KEY` | (vazio) | Não* | API Key da OpenAI |

> \* Sem `OPENAI_API_KEY`, a avaliação STAR usa um fallback com score padrão de 5.0.

### Gerar um JWT_SECRET seguro para produção

```bash
openssl rand -base64 64
```

---

## 6. Executando a Aplicação

### Modo desenvolvimento

```bash
# Com Docker Compose (tudo junto)
docker compose up -d

# Ou apenas a API localmente
docker compose up -d postgres
mvn spring-boot:run
```

### Modo produção (JAR)

```bash
mvn clean package -DskipTests

DB_HOST=seu-host \
DB_USER=seu-user \
DB_PASSWORD=sua-senha \
JWT_SECRET=sua-chave-base64 \
OPENAI_API_KEY=sk-xxx \
java -jar target/careerflow-api-0.1.0-SNAPSHOT.jar
```

### Rebuild do Docker

```bash
docker compose build --no-cache
docker compose up -d
```

---

## 7. Executando os Testes

### Testes unitários + integração

```bash
mvn test
```

Os testes usam H2 em memória (não precisam de PostgreSQL).

### Testes incluídos

| Teste | Tipo | O que valida |
|---|---|---|
| `CareerFlowApplicationTests` | Integração | Contexto Spring carrega corretamente |
| `StarEvaluationTest` (4 testes) | Unitário | Fórmula de cálculo do score STAR |
| `AuthServiceTest` (2 testes) | Unitário | Registro de usuário e validação de email duplicado |
| `AuthControllerIntegrationTest` (4 testes) | Integração | Registro, login, validação de email e senha |

### Executar um teste específico

```bash
mvn test -Dtest=StarEvaluationTest
mvn test -Dtest=AuthControllerIntegrationTest
```

### Gerar relatório de testes

```bash
mvn surefire-report:report
# Relatório em: target/site/surefire-report.html
```

---

## 8. Acessando a Documentação (Swagger)

Após iniciar a aplicação, acesse:

- **Swagger UI:** http://localhost:8080/api/v1/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api/v1/api-docs

### Autenticação no Swagger

1. Execute o endpoint `POST /auth/login` com as credenciais seed
2. Copie o `accessToken` da resposta
3. Clique no botão **"Authorize"** (cadeado) no topo do Swagger
4. Cole: `Bearer <seu-token>` → Authorize
5. Agora todos os endpoints autenticados funcionarão

---

## 9. Configurando a Integração com OpenAI

A integração com OpenAI é **opcional**. Sem ela, o sistema funciona normalmente usando um fallback com score padrão.

### Para ativar

1. Crie uma API Key em https://platform.openai.com/api-keys
2. Configure a variável:

```bash
# Via .env
OPENAI_API_KEY=sk-proj-xxxxx

# Via Docker Compose
docker compose down
OPENAI_API_KEY=sk-proj-xxxxx docker compose up -d

# Via linha de comando
OPENAI_API_KEY=sk-proj-xxxxx mvn spring-boot:run
```

### Modelo utilizado

O sistema usa `gpt-4o-mini` por padrão (configurável em `application.yml`).

### Resiliência

Se a OpenAI estiver indisponível:
- **Retry:** 3 tentativas automáticas
- **Circuit Breaker:** Abre após 50% de falhas em 10 chamadas
- **Fallback:** Retorna score 5.0 com feedback padrão

---

## 10. Testando os Endpoints

### Via cURL

```bash
# 1. Login como admin
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@careerflow.com","password":"admin123!"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['accessToken'])")

echo "Token: $TOKEN"

# 2. Listar desafios (público)
curl -s http://localhost:8080/api/v1/challenges | python3 -m json.tool

# 3. Ver dashboard (requer ADMIN)
curl -s -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/analytics/dashboard | python3 -m json.tool

# 4. Buscar usuário atual
curl -s -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/users/me | python3 -m json.tool
```

### Via Postman

1. Importe o arquivo `docs/CareerFlow.postman_collection.json` no Postman
2. A collection já inclui scripts de teste que salvam o token automaticamente
3. Execute o request **"Login"** primeiro — o token será salvo na variável `{{accessToken}}`
4. Todos os demais requests usarão o token automaticamente

### Fluxo completo de teste

```bash
# 1. Login como estudante
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"maria@student.com","password":"admin123!"}' > /tmp/login.json

STUDENT_TOKEN=$(python3 -c "import json; print(json.load(open('/tmp/login.json'))['accessToken'])")

# 2. Submeter resposta STAR
curl -s -X POST http://localhost:8080/api/v1/submissions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $STUDENT_TOKEN" \
  -d '{
    "challengeId": "10000000-0000-0000-0000-000000000001",
    "situation": "Na empresa onde eu trabalhava, o sistema estava desatualizado e causava lentidão.",
    "task": "Fui designado para redesenhar a API principal do sistema.",
    "action": "Implementei usando Spring Boot 3 com Clean Architecture, JWT e documentação Swagger.",
    "result": "O tempo de resposta caiu 60% e a equipe ganhou produtividade com a documentação."
  }' | python3 -m json.tool

# 3. Login como empresa e buscar matching
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"rh@techcorp.com","password":"admin123!"}' > /tmp/company.json

COMPANY_TOKEN=$(python3 -c "import json; print(json.load(open('/tmp/company.json'))['accessToken'])")

curl -s -X POST http://localhost:8080/api/v1/matching/search \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $COMPANY_TOKEN" \
  -d '{"skills": ["Java", "Spring Boot"], "minScore": 0.0}' | python3 -m json.tool
```

---

## 11. Troubleshooting

### Porta 5432 já em uso

```bash
# Verificar processo
sudo lsof -i :5432

# Alterar porta no docker-compose.yml
ports:
  - "5433:5432"  # usar 5433 externamente

# Executar com porta diferente
DB_PORT=5433 mvn spring-boot:run
```

### Porta 8080 já em uso

```bash
# Verificar processo
sudo lsof -i :8080

# Executar em outra porta
SERVER_PORT=9090 mvn spring-boot:run
```

### Erro de conexão com o banco

```bash
# Verificar se o PostgreSQL está rodando
docker compose ps
docker compose logs postgres

# Testar conexão
docker exec -it careerflow-db pg_isready -U careerflow
```

### Flyway migration error

```bash
# Limpar o banco e recriar
docker compose down -v
docker compose up -d

# Ou reparar manualmente
docker exec -it careerflow-db psql -U careerflow -d careerflow -c "DELETE FROM flyway_schema_history WHERE success = false;"
```

### Java version mismatch

```bash
# Verificar versão
java --version

# Se não for 21, usar SDKMAN
sdk use java 21.0.5-tem
```

### Docker build lento

```bash
# Usar build cache
docker compose build

# Ou forçar rebuild sem cache
docker compose build --no-cache
```

---

## 12. Estrutura do Projeto

```
careerflow-api/
├── pom.xml                          # Dependências Maven
├── Dockerfile                       # Build multi-stage (JDK → JRE)
├── docker-compose.yml               # PostgreSQL + API
├── .gitignore
├── README.md                        # Visão geral do projeto
├── SETUP.md                         # Este arquivo
├── docs/
│   ├── CareerFlow.postman_collection.json
│   └── adr/
│       ├── 001-clean-architecture.md
│       ├── 002-jwt-authentication.md
│       ├── 003-openai-integration.md
│       ├── 004-event-driven-architecture.md
│       └── 005-database-strategy.md
└── src/
    ├── main/
    │   ├── java/com/careerflow/
    │   │   ├── CareerFlowApplication.java
    │   │   ├── domain/
    │   │   │   ├── entities/          # User, StudentProfile, Company, Challenge, Submission, StarEvaluation
    │   │   │   ├── events/            # SubmissionCreatedEvent, EvaluationCompletedEvent
    │   │   │   ├── repositories/      # JPA Repository interfaces
    │   │   │   └── valueobjects/      # Role, Difficulty
    │   │   ├── application/
    │   │   │   ├── dto/               # Request/Response records
    │   │   │   ├── gateways/          # AiEvaluationGateway interface
    │   │   │   ├── services/          # Auth, User, Student, Company, Challenge, Submission, Evaluation, Analytics, Matching
    │   │   │   └── usecases/          # UpdateStudentScoreUseCase
    │   │   ├── infrastructure/
    │   │   │   ├── config/            # Security, OpenAPI, Async, Resilience4j
    │   │   │   ├── security/          # JWT, UserPrincipal, Filters
    │   │   │   ├── clients/           # OpenAI Feign Client + Gateway
    │   │   │   └── observability/     # Request Logging Filter
    │   │   └── interfaces/
    │   │       ├── rest/              # Controllers (Auth, User, Student, Company, Challenge, Submission, Evaluation, Analytics, Matching)
    │   │       └── handlers/          # GlobalExceptionHandler
    │   └── resources/
    │       ├── application.yml
    │       └── db/migration/          # V1..V7 Flyway migrations
    └── test/
        ├── java/com/careerflow/
        │   ├── CareerFlowApplicationTests.java
        │   ├── domain/StarEvaluationTest.java
        │   ├── application/AuthServiceTest.java
        │   └── interfaces/AuthControllerIntegrationTest.java
        └── resources/
            └── application-test.yml
```

---

## Usuários Seed para Testes

| Email | Senha | Role | Perfil |
|---|---|---|---|
| admin@careerflow.com | admin123! | ADMIN | — |
| maria@student.com | admin123! | STUDENT | Eng. Software, Universidade XYZ |
| rh@techcorp.com | admin123! | COMPANY | TechCorp Solutions, Technology |
| coord@unixyz.edu.br | admin123! | INSTITUTION | — |

> **Nota:** Todas as senhas seed usam o mesmo hash BCrypt para simplificar o desenvolvimento. Em produção, cada usuário terá sua própria senha.
