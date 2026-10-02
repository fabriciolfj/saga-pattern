package com.exemplo.saga.orquestrador.service.inicializacao;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.domain.Transacao;
import com.exemplo.saga.orquestrador.service.fraude.FraudeCommandService;
import com.exemplo.saga.orquestrador.service.saga.SagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionInicializacaoService {

    private final JsonMapper jsonMapper;
    private final FraudeCommandService fraudeCommandService;
    private final SagaService sagaService;

    @Transactional
    public Saga execute(final Transacao transacao) {
        final var saga = SagaInicializacaoMapper.toDomain(transacao, jsonMapper.writeValueAsString(transacao));

        try (var ignored = MDC.putCloseable("correlationId", saga.getId())) {
            log.info("saga started sagaId={} transactionId={} etapa={} status={}",
                    saga.getId(), transacao.transactionId(), saga.getEtapaAtual(), saga.getStatus());

            sagaService.executePersist(saga);
            fraudeCommandService.sendCommand(saga);
        }

        return saga;
    }
}
