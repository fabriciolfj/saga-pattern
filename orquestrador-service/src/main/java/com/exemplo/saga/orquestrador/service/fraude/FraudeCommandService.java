package com.exemplo.saga.orquestrador.service.fraude;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.service.outbox.OutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.exemplo.saga.orquestrador.service.fraude.FraudeMapper.toCommando;

@Slf4j
@Service
public class FraudeCommandService {

    private final String topic;
    private final OutboxService outboxService;

    public FraudeCommandService(@Value("${topicos.fraude.entrada}") final String topic,
                                final OutboxService outboxService) {
        this.topic = topic;
        this.outboxService = outboxService;
    }

    public void sendCommand(final Saga saga) {
        final ContratoFraude.AnalisarTransacao fraudCommand = toCommando(saga);
        outboxService.registrar(UUID.fromString(saga.getId()), topic, fraudCommand);

        log.info("fraud command queued sagaId={} topic={}", saga.getId(), topic);
    }
}
