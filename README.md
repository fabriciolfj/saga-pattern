# saga-pix

Quatro **microserviços independentes** — cada um com seu próprio build Gradle, seu
Dockerfile e seu ciclo de vida. Não há projeto pai nem repositório agregador: você
pode mover qualquer pasta para um repositório próprio sem tocar em nada.

```
saga-pix/
├── docker-compose.yml          infra compartilhada (e os serviços, no profile apps)
├── orquestrador-service/       ← ESQUELETO, você monta
├── fraude-service/
├── cancelamento-service/
└── pix-service/
```

## O fluxo

```
POST /transacoes
   └─> fraude.analisar ──> [antifraude sorteia] ──> fraude.analisado
                                                        │
                        fraudulenta? ───────────────────┤
                                │                       │
                              true                    false
                                │                       │
                   cancelamento.cancelar          pix.depositar
                                │                       │
                   cancelamento.cancelado        pix.depositado
                                │                       │
                            ABORTADA                CONCLUIDA
```

| Serviço | Porta | Estado | Banco |
|---|---|---|---|
| `orquestrador-service` | 8080 | **esqueleto** | PostgreSQL + Flyway |
| `fraude-service` | 8081 | pronto | — |
| `cancelamento-service` | 8082 | pronto | — |
| `pix-service` | 8083 | pronto | — |

Stack: Java 25, Spring Boot 4, Spring Kafka, Gradle (Kotlin DSL).

## Contratos divergentes, de propósito

Cada serviço nomeia as coisas do seu jeito, como seria se times diferentes os
tivessem escrito. Traduzir entre eles é justamente o trabalho do orquestrador.

| | identificador | valor | extras |
|---|---|---|---|
| fraude | `transacaoId` | `BigDecimal valor` | `documentoCliente` |
| cancelamento | `codigoTransacao` | — | `motivoCancelamento` |
| pix | `txid` | `long valorCentavos` | `chavePix` |

O detalhe que morde: o pix quer **centavos**, o antifraude usa **BigDecimal**. E a
`chavePix` não volta em nenhuma resposta — veio no POST e some, a menos que o
orquestrador a guarde em `saga_instance.dados`. É isso que torna a tabela necessária
em vez de opcional.

## Subir

**Infra primeiro** (Postgres, Kafka, kafka-ui em 8090):

```bash
docker compose up -d
```

**Serviços pela IDE ou pelo terminal**, cada um na sua pasta:

```bash
cd fraude-service       && ./gradlew bootRun
cd cancelamento-service && ./gradlew bootRun
cd pix-service          && ./gradlew bootRun
cd orquestrador-service && ./gradlew bootRun
```

**Ou tudo em container:**

```bash
for s in orquestrador fraude cancelamento pix; do (cd $s-service && ./gradlew bootJar); done
docker compose --profile apps up -d --build
```

Os Dockerfiles copiam `build/libs/app.jar`, então o `bootJar` tem que rodar antes.

## Testar

```bash
curl -s -X POST localhost:8080/transacoes \
  -H 'Content-Type: application/json' \
  -d '{"valor":250.00,"documentoCliente":"12345678900","chavePix":"fabricio@email.com"}'

curl -s localhost:8080/sagas/{sagaId}
```

Esses dois endpoints só existem depois que você montar o orquestrador. Até lá, dá
para testar os participantes isolados publicando pelo kafka-ui (localhost:8090) em `fraude.analisar`:

```json
{"transacaoId":"t-1","valor":250.00,"documentoCliente":"12345678900"}
```

A resposta aparece em `fraude.analisado` com o veredito sorteado.

## Gradle wrapper

Cada serviço tem o seu wrapper completo (`gradlew`, `gradlew.bat` e
`gradle/wrapper/`), fixado no Gradle 9.7.1 — Java 25 exige Gradle 9.1 ou mais novo.
Não é preciso ter Gradle instalado:

```bash
cd fraude-service && ./gradlew build
```

## Importar no IntelliJ

Cada serviço é um projeto independente. Abra **um por janela**, apontando para o
`build.gradle.kts` da pasta do serviço. SDK Java 25.

Se preferir ver os quatro juntos, abra um deles e use `File > New > Module from
Existing Sources` para os outros três — mas lembre que eles não compartilham build.
