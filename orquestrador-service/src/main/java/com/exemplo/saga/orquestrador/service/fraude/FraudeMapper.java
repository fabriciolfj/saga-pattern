package com.exemplo.saga.orquestrador.service.fraude;

import com.exemplo.saga.orquestrador.domain.Saga;

public final class FraudeMapper {

    private FraudeMapper() { }

    /**
     * transacaoId recebe o sagaId (correlationId), não o id do cliente: o antifraude devolve
     * esse valor em TransacaoAnalisada e é por ele que o orquestrador acha a saga.
     */
    public static ContratoFraude.AnalisarTransacao toCommando(final Saga saga) {
        return ContratoFraude.AnalisarTransacao.builder()
                .transacaoId(saga.getId())
                .documentoCliente(saga.getTransacao().documentCustomer())
                .valor(saga.getTransacao().value())
                .build();
    }
}
