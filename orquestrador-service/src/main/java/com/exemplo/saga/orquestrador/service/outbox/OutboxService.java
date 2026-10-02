package com.exemplo.saga.orquestrador.service.outbox;

import com.exemplo.saga.orquestrador.repository.OutboxEntity;
import com.exemplo.saga.orquestrador.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final JsonMapper jsonMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(final UUID correlationId, final String topico, final Object mensagem) {
        final var outbox = OutboxEntity.builder()
                .id(UUID.randomUUID())
                .correlationId(correlationId)
                .topico(topico)
                .payload(jsonMapper.writeValueAsString(mensagem))
                .build();

        outboxRepository.save(outbox);

        log.info("outbox message registered id={} correlationId={} topic={}", outbox.getId(), correlationId, topico);
    }
}
