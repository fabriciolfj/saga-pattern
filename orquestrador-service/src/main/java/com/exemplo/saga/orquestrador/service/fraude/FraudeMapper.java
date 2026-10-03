package com.exemplo.saga.orquestrador.service.fraude;

import com.exemplo.saga.orquestrador.domain.Saga;

public final class FraudeMapper {

    private FraudeMapper() { }


    public static ContratoFraude.AnalisarTransacao toCommando(final Saga saga) {
        return ContratoFraude.AnalisarTransacao.builder()
                .transacaoId(saga.getIdTransacao())
                .documentoCliente(saga.getTransacao().documentCustomer())
                .valor(saga.getTransacao().value())
                .build();
    }
}
