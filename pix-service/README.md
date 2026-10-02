# pix-service

Deposita na conta do cliente — por enquanto um log. Toda a lógica em
`DepositoPixListener`.

- Consome `pix.depositar`
- Produz `pix.depositado`
- Porta 8083

## Contrato

Entrada:

```json
{"txid":"t-1","chavePix":"fabricio@email.com","valorCentavos":25000}
```

Saída:

```json
{"txid":"t-1","status":"DEPOSITADO","comprovante":"E1735..."}
```

Três divergências em relação ao antifraude: o id é `txid`, o valor vem em
**centavos** (`long`) e existe uma `chavePix` que nenhuma resposta devolve — ela
veio no POST inicial e só o orquestrador a tem guardada.

## Rodar

```bash
./gradlew bootRun
```
