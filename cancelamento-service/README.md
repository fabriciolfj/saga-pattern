# cancelamento-service

Recebe e cancela. Não decide nada — a decisão veio do orquestrador.
Toda a lógica em `CancelamentoListener`.

- Consome `cancelamento.cancelar`
- Produz `cancelamento.cancelado`
- Porta 8082

## Contrato

Entrada:

```json
{"codigoTransacao":"t-1","motivoCancelamento":"fraude detectada"}
```

Saída:

```json
{"codigoTransacao":"t-1","canceladoEm":"2026-01-01T12:00:00Z"}
```

Note que aqui o identificador é `codigoTransacao`, não `transacaoId`.

## Rodar

```bash
./gradlew bootRun
```
