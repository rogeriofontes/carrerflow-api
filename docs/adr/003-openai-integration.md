# ADR-003: Integração com OpenAI para Avaliação STAR

## Status
Aceito

## Contexto
O sistema precisa avaliar submissões STAR automaticamente com feedback detalhado e score de competências.

## Decisão
- Integração via OpenFeign com a API da OpenAI
- Circuit Breaker (Resilience4j) para proteção contra falhas
- Retry automático com 3 tentativas
- Fallback com score padrão quando IA indisponível
- Avaliação assíncrona via Spring Events + @Async

## Consequências
- Avaliação automática sem intervenção humana
- Resiliência: sistema continua operando mesmo com OpenAI offline
- Custos por chamada à API (necessário monitorar uso)
- Feedback em português para melhor UX
