package com.exemplo.saga.orquestrador.controller;

import com.exemplo.saga.orquestrador.domain.Transacao;

public final class TransacaoRequestMapper {

    private TransacaoRequestMapper() { }

    public static Transacao toTransaction(final TransacaoRequest request) {
        return Transacao.builder()
                .transactionId(request.transactionId())
                .documentCustomer(request.documentCustomer())
                .value(request.value())
                .build();
    }
}
