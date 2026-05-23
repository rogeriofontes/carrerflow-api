# ADR-001: Clean Architecture como Padrão Arquitetural

## Status
Aceito

## Contexto
O CareerFlow precisa de uma arquitetura que suporte evolução futura, incluindo possível migração para microsserviços, multi-tenant e deploy em cloud.

## Decisão
Adotar Clean Architecture com as seguintes camadas:
- **domain**: Entidades, Value Objects, Repositórios (interfaces), Domain Events
- **application**: Use Cases, DTOs, Gateways (interfaces), Services
- **infrastructure**: Configurações, Segurança, Persistência JPA, Clientes externos, Observabilidade
- **interfaces**: REST Controllers, Mappers, Exception Handlers

## Consequências
- Alta testabilidade: domain e application testáveis sem dependências externas
- Inversão de dependência: camadas internas não dependem de externas
- Facilita extração futura de módulos em microsserviços
- Complexidade inicial maior, porém escalável
