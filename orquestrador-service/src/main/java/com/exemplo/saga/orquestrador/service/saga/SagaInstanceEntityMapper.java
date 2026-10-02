package com.exemplo.saga.orquestrador.service.saga;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.repository.SagaInstanceEntity;

import java.util.UUID;

public class SagaInstanceEntityMapper {

    private SagaInstanceEntityMapper() { }

    public static SagaInstanceEntity toEntity(final Saga saga) {
        return SagaInstanceEntity.builder()
                .sagaId(UUID.fromString(saga.getId()))
                .status(saga.getStatus())
                .etapa(saga.getEtapaAtual())
                .dados(saga.getPayload())
                .build();
    }
}
