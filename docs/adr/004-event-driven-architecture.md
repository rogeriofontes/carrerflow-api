# ADR-004: Event-Driven Architecture com Spring Events

## Status
Aceito

## Contexto
Operações como avaliação de submissão e atualização de score são processos assíncronos que não devem bloquear o fluxo principal.

## Decisão
- Spring Application Events para comunicação entre componentes
- @Async para processamento assíncrono
- Domain Events: SubmissionCreatedEvent, EvaluationCompletedEvent

## Fluxo
1. Estudante submete resposta STAR → SubmissionCreatedEvent
2. StarEvaluationService escuta e aciona OpenAI → EvaluationCompletedEvent
3. UpdateStudentScoreUseCase recalcula score do estudante

## Consequências
- Baixo acoplamento entre módulos
- Processamento assíncrono melhora tempo de resposta
- Facilita migração futura para mensageria (RabbitMQ/Kafka)
- Necessário tratar falhas em listeners assíncronos
