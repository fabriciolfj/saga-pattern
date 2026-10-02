# fraude-service

Analisa uma transação e devolve um veredito **sorteado** (`ThreadLocalRandom`).
Sem banco, sem camadas: `AnaliseFraudeListener` tem toda a lógica.

- Consome `fraude.analisar`
- Produz `fraude.analisado`
- Porta 8081

## Contrato

Entrada:

```json
{"transacaoId":"t-1","valor":250.00,"documentoCliente":"12345678900"}
```

Saída:

```json
{"transacaoId":"t-1","fraudulenta":true,"score":0.87}
```

O identificador aqui se chama `transacaoId` e o valor é `BigDecimal` — os outros
serviços usam outros nomes e outros tipos. Traduzir é trabalho do orquestrador.

## Rodar

```bash
./gradlew bootRun
```

Precisa do Kafka em `localhost:9092` (`docker compose up -d` na raiz).
