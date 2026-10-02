# orquestrador-service — ESQUELETO

Decide o fluxo da transação. **Só existe a classe principal e os pacotes vazios** —
todo o resto é seu.

- Consome `fraude.analisado`, `cancelamento.cancelado`, `pix.depositado`
- Produz `fraude.analisar`, `cancelamento.cancelar`, `pix.depositar`
- Porta 8080, PostgreSQL + Flyway

## O que existe

| | |
|---|---|
| `OrquestradorApplication` | a classe principal |
| `api/` | vazio — `POST /transacoes` e `GET /sagas/{id}` |
| `config/` | **pronto** — `TopicosProperties` (nomes, lidos de `topicos.*` no `application.yml`) e `KafkaConfig` (cria os seis tópicos na subida) |
| `contrato/` | **pronto** — `ContratoFraude`, `ContratoCancelamento` e `ContratoPix`, como os participantes os definiram |
| `kafka/` | vazio — publicação e os listeners das respostas |
| `repository/` | vazio — persistência de `saga_instance` |
| `application.yml` | PostgreSQL (datasource, pool, Flyway) e Kafka já apontando para o `docker-compose.yml` |
| `V1__init.sql` | tabela `saga_instance` |

## Falta implementar

A máquina de estados inteira:

```
POST /transacoes        -> gravar contexto + publicar AnalisarTransacao
fraude.analisado        -> a decisão: cancelar ou depositar
cancelamento.cancelado  -> encerrar como ABORTADA
pix.depositado          -> encerrar como CONCLUIDA
```

Três decisões antes de escrever:

**Onde guardar o contexto.** `chavePix` e `valor` vieram no POST e não voltam em
nenhuma resposta. Sem gravá-los em `saga_instance.dados`, o comando do pix não tem
como ser montado dois passos depois.

**Quem traduz.** `BigDecimal` → centavos, `transacaoId` → `txid` → `codigoTransacao`.
Os participantes não se adaptam.

**Atomicidade.** Gravar a etapa e publicar no Kafka não são atômicos: se o publish
falhar depois do commit, a saga trava sem ninguém para respondê-la. A solução é a
Transactional Outbox — gravar a mensagem numa tabela no mesmo commit e publicar
depois, por um scheduler.

## Rodar

```bash
./gradlew bootRun
```

Precisa de Postgres e Kafka (`docker compose up -d` na raiz).
