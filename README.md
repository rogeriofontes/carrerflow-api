# CareerFlow API

Plataforma de empregabilidade acadêmica baseada em evidências reais de competências.

## Stack Tecnológica

| Tecnologia | Versão | Propósito |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.3.3 | Framework backend |
| PostgreSQL | 16 | Banco de dados |
| Flyway | - | Migrations de banco |
| Spring Security + JWT | - | Autenticação e autorização |
| MapStruct | 1.5.5 | Mapeamento de objetos |
| OpenFeign | 4.1.3 | Cliente HTTP declarativo |
| Resilience4j | 2.2.0 | Circuit Breaker, Retry |
| SpringDoc OpenAPI | 2.6.0 | Documentação Swagger |
| OpenTelemetry + Micrometer | - | Observabilidade |
| Docker + Docker Compose | - | Containerização |
| Lombok | - | Redução de boilerplate |

## Arquitetura

O projeto segue **Clean Architecture** com **DDD** e **Event-Driven Architecture**:

```
src/main/java/com/careerflow
├── domain/           # Entidades, Value Objects, Repositórios (interfaces), Domain Events
├── application/      # Use Cases, DTOs, Gateways (interfaces), Services
├── infrastructure/   # Config, Security, Persistence, Clients, Observability
└── interfaces/       # REST Controllers, Mappers, Exception Handlers
```

### Módulos

1. **Auth Module** - Autenticação JWT com RBAC
2. **User Module** - Gestão de usuários
3. **Student Profile Module** - Perfis de estudantes com skills
4. **Company Module** - Perfis de empresas
5. **Challenge Module** - Desafios práticos
6. **Submission Module** - Submissão de respostas STAR
7. **STAR Evaluation Module** - Avaliação automática com IA
8. **AI Feedback Module** - Integração com OpenAI
9. **Analytics Module** - Dashboard e métricas
10. **Matching Module** - Matching empresa-estudante

## Quick Start

### Pré-requisitos

- Docker e Docker Compose
- Java 21 (para desenvolvimento local)
- Maven 3.9+

### Executar com Docker Compose

```bash
# Subir todos os serviços
docker compose up -d

# A API estará disponível em: http://localhost:8080/api/v1
# Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
```

### Executar localmente

```bash
# 1. Subir apenas o PostgreSQL
docker compose up -d postgres

# 2. Compilar e executar
mvn clean spring-boot:run
```

### Executar testes

```bash
mvn test
```

## Endpoints Principais

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/auth/register` | Registrar usuário | Público |
| POST | `/auth/login` | Login | Público |
| GET | `/users/me` | Perfil do usuário atual | Autenticado |
| POST | `/students` | Criar perfil de estudante | STUDENT |
| POST | `/companies` | Criar perfil de empresa | COMPANY |
| GET | `/challenges` | Listar desafios | Público |
| POST | `/challenges` | Criar desafio | COMPANY/ADMIN |
| POST | `/submissions` | Submeter resposta STAR | STUDENT |
| GET | `/evaluations/submission/{id}` | Ver avaliação | Autenticado |
| GET | `/analytics/dashboard` | Dashboard | ADMIN/INSTITUTION |
| POST | `/matching/search` | Buscar estudantes | COMPANY/ADMIN |

## Usuários Seed

| Email | Senha | Role |
|---|---|---|
| admin@careerflow.com | admin123! | ADMIN |
| maria@student.com | admin123! | STUDENT |
| rh@techcorp.com | admin123! | COMPANY |
| coord@unixyz.edu.br | admin123! | INSTITUTION |

## Score STAR

O score final é calculado com pesos diferentes para cada componente:

```
score = (situation × 0.2) + (task × 0.2) + (action × 0.3) + (result × 0.3)
```

## Variáveis de Ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_HOST` | localhost | Host do PostgreSQL |
| `DB_PORT` | 5432 | Porta do PostgreSQL |
| `DB_NAME` | careerflow | Nome do banco |
| `DB_USER` | careerflow | Usuário do banco |
| `DB_PASSWORD` | careerflow | Senha do banco |
| `JWT_SECRET` | (dev key) | Chave secreta JWT (Base64) |
| `OPENAI_API_KEY` | - | Chave da API OpenAI |

## ADRs (Architecture Decision Records)

- [ADR-001: Clean Architecture](docs/adr/001-clean-architecture.md)
- [ADR-002: JWT Authentication](docs/adr/002-jwt-authentication.md)
- [ADR-003: OpenAI Integration](docs/adr/003-openai-integration.md)
- [ADR-004: Event-Driven Architecture](docs/adr/004-event-driven-architecture.md)
- [ADR-005: Database Strategy](docs/adr/005-database-strategy.md)

## Postman Collection

Importe o arquivo `docs/CareerFlow.postman_collection.json` no Postman para testar todos os endpoints.

## Observabilidade

- **Structured Logging** com traceId e spanId
- **Spring Actuator** endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`
- **OpenTelemetry** para tracing distribuído
- **Request Logging Filter** com tempo de resposta

## Resiliência

- **Circuit Breaker** para chamadas à OpenAI (Resilience4j)
- **Retry** automático com 3 tentativas
- **Fallback** com score padrão quando IA indisponível
- **Timeout** configurável para chamadas externas
