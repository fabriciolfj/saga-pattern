package com.exemplo.saga.orquestrador.service.saga;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.domain.Transacao;
import com.exemplo.saga.orquestrador.repository.SagaInstanceEntity;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

public final class SagaInstanceEntityMapper {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    private SagaInstanceEntityMapper() { }

    public static SagaInstanceEntity toEntity(final Saga saga) {
        return SagaInstanceEntity.builder()
                .sagaId(UUID.fromString(saga.getId()))
                .transactionId(saga.getTransacao().transactionId())
                .status(saga.getStatus())
                .etapa(saga.getEtapaAtual())
                .dados(saga.getPayload())
                .build();
    }

    public static Saga toDomain(final SagaInstanceEntity entity) {
        return Saga.builder()
                .id(entity.getSagaId().toString())
                .status(entity.getStatus())
                .etapaAtual(entity.getEtapa())
                .payload(entity.getDados())
                .transacao(JSON_MAPPER.readValue(entity.getDados(), Transacao.class))
                .version(entity.getVersion())
                .build();
    }
}
