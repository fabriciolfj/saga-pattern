package com.exemplo.saga.orquestrador.service.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxPublisher outboxPublisher;

    @Scheduled(fixedDelayString = "${outbox.intervalo-ms}")
    public void executar() {
        outboxPublisher.publicarPendentes();
    }
}
