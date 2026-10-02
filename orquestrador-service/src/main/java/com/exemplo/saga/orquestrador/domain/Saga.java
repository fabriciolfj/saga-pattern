package com.exemplo.saga.orquestrador.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Saga {

    private String id;
    private Status status;
    private Etapa etapaAtual;
    private String payload;
    private Transacao transacao;

    @Builder
    public Saga(String id, Status status, Etapa etapaAtual, String payload, Transacao transacao) {
        this.id = id;
        this.status = status;
        this.etapaAtual = etapaAtual;
        this.payload = payload;
        this.transacao = transacao;
    }
}
