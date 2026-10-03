package com.exemplo.saga.orquestrador.service.cancelamento;

import com.exemplo.saga.orquestrador.domain.Saga;

public final class CancelamentoMapper {

    private static final String MOTIVO_FRAUDE = "Transacao identificada como fraudulenta";

    private CancelamentoMapper() {
    }

    public static ContratoCancelamento.CancelarTransacao toCancelarTransacao(final Saga saga) {
        return ContratoCancelamento.CancelarTransacao.builder()
                .codigoTransacao(saga.getIdTransacao())
                .motivoCancelamento(MOTIVO_FRAUDE)
                .build();
    }
}
