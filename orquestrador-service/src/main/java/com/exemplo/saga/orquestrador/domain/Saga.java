package com.exemplo.saga.orquestrador.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Saga {

    private String id;
    private Status status;
    private Etapa etapaAtual;
    private String payload;
    private Transacao transacao;
    private Integer version;

    @Builder
    public Saga(String id, Status status, Etapa etapaAtual, String payload, Transacao transacao, Integer version) {
        this.id = id;
        this.status = status;
        this.etapaAtual = etapaAtual;
        this.payload = payload;
        this.transacao = transacao;
        this.version = version;
    }

    public void startFraudAnalysis() {
        requireEtapa(Etapa.INICIALIZACAO);
        this.etapaAtual = etapaAtual.next();
    }

    public Etapa analyseFraud(boolean isFraud) {
        requireEtapa(Etapa.FRAUDE);
        this.etapaAtual = etapaAtual.next(isFraud);
        return this.etapaAtual;
    }

    public boolean isAwaiting(final Etapa etapa) {
        return status.equals(Status.EXECUTANDO) && etapaAtual.equals(etapa);
    }

    public String getIdTransacao() {
        return transacao.transactionId();
    }

    public String getDocumentCustomer() {
        return transacao.documentCustomer();
    }

    public BigDecimal getValue() {
        return transacao.value();
    }

    private void requireEtapa(final Etapa esperada) {
        if (etapaAtual != esperada) {
            throw new IllegalStateException("saga " + id + " is at " + etapaAtual + ", expected " + esperada);
        }
    }
}
