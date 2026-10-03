package com.exemplo.saga.orquestrador.service.deposito;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.kafka.ProducerService;
import com.exemplo.saga.orquestrador.service.common.StepEndService;
import com.exemplo.saga.orquestrador.service.outbox.OutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.exemplo.saga.orquestrador.util.Constants.DESC_DEPOSITO;
import static com.exemplo.saga.orquestrador.util.JsonMapperUtil.JSON_MAPPER;

@Slf4j
@Service(DESC_DEPOSITO)
public class DepositoStepService implements StepEndService {

    private final OutboxService outboxService;
    private final String topic;

    public DepositoStepService(final OutboxService outboxService,
                               @Value("${topicos.pix.entrada}")
                               final String topic) {
        this.outboxService = outboxService;
        this.topic = topic;
    }

    @Override
    public void execute(final Saga saga) {
        final ContratoPix.DepositarPix depositarPix = DepositMapper.toDepositarPix(saga);
        final String json = JSON_MAPPER.writeValueAsString(depositarPix);

        outboxService.registrar(UUID.fromString(saga.getId()), topic, json);
        log.info("registred outbox deposit saga {}", saga.getId());
    }
}
