# ADR-005: PostgreSQL com Flyway Migrations

## Status
Aceito

## Contexto
O sistema precisa de um banco relacional robusto com controle de versão de schema.

## Decisão
- PostgreSQL 16 como banco principal
- UUIDs como chaves primárias (preparação para distribuição)
- Flyway para versionamento de migrations
- Seed data via migration V7
- HikariCP para connection pooling
- JPA/Hibernate com ddl-auto=validate (schema controlado pelo Flyway)

## Consequências
- Controle total do schema via migrations versionadas
- UUIDs previnem colisões em cenários distribuídos
- PostgreSQL oferece performance e recursos avançados
- Seed data facilita desenvolvimento e testes
