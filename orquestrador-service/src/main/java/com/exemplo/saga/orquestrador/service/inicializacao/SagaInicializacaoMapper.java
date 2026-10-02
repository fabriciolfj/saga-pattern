package com.exemplo.saga.orquestrador.service.inicializacao;

import com.exemplo.saga.orquestrador.domain.Etapa;
import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.domain.Status;
import com.exemplo.saga.orquestrador.domain.Transacao;
import java.util.UUID;

public final class SagaInicializacaoMapper {

    private SagaInicializacaoMapper() { }

    public static Saga toDomain(final Transacao transacao, final String payload) {
        return Saga.builder()
                .id(UUID.randomUUID().toString())
                .status(Status.EXECUTANDO)
                .etapaAtual(Etapa.INICIALIZACAO)
                .payload(payload)
                .transacao(transacao)
                .build();
    }
}
