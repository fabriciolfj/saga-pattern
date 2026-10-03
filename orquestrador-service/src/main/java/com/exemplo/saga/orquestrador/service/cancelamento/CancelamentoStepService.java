package com.exemplo.saga.orquestrador.service.cancelamento;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.service.common.StepEndService;
import com.exemplo.saga.orquestrador.service.outbox.OutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.exemplo.saga.orquestrador.util.Constants.DESC_CANCELAMENTO;
import static com.exemplo.saga.orquestrador.util.JsonMapperUtil.JSON_MAPPER;

@Slf4j
@Service(DESC_CANCELAMENTO)
public class CancelamentoStepService implements StepEndService {

    private final OutboxService outboxService;
    private final String topic;

    public CancelamentoStepService(final OutboxService outboxService,
                                   final @Value("${topicos.cancelamento.entrada}") String topic) {
        this.outboxService = outboxService;
        this.topic = topic;
    }

    @Override
    public void execute(final Saga saga) {
        final var cancelarTransacao = CancelamentoMapper.toCancelarTransacao(saga);
        final String json = JSON_MAPPER.writeValueAsString(cancelarTransacao);

        outboxService.registrar(UUID.fromString(saga.getId()), topic, json);
        log.info("registred outbox cancel saga {}", saga.getId());
    }
}
