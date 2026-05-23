# ADR-002: JWT para Autenticação Stateless

## Status
Aceito

## Contexto
O sistema precisa de autenticação que suporte:
- Múltiplos clientes (web, mobile)
- Escalabilidade horizontal
- RBAC (Role-Based Access Control)

## Decisão
- JWT (JSON Web Token) com Spring Security
- BCrypt para hash de senhas
- Access Token + Refresh Token
- Roles: ADMIN, STUDENT, COMPANY, INSTITUTION
- Sessão completamente stateless

## Consequências
- Escalabilidade horizontal sem necessidade de sessão compartilhada
- Tokens auto-contidos reduzem consultas ao banco
- Necessidade de gerenciar expiração e refresh
- Impossibilidade de invalidar token individual (trade-off aceito)
