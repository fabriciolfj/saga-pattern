package com.exemplo.saga.orquestrador.service.outbox;

import com.exemplo.saga.orquestrador.kafka.ProducerService;
import com.exemplo.saga.orquestrador.repository.OutboxEntity;
import com.exemplo.saga.orquestrador.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final ProducerService producerService;
    private final int lote;

    public OutboxPublisher(final OutboxRepository outboxRepository,
                           final ProducerService producerService,
                           @Value("${outbox.lote}") final int lote) {
        this.outboxRepository = outboxRepository;
        this.producerService = producerService;
        this.lote = lote;
    }

    @Transactional
    public void publicarPendentes() {
        final var pendentes = outboxRepository.buscarPendentesParaPublicar(lote);
        if (pendentes.isEmpty()) {
            return;
        }

        log.debug("publishing {} outbox messages", pendentes.size());

        for (final OutboxEntity mensagem : pendentes) {
            if (!publicar(mensagem)) {
                break;
            }
        }
    }

    private boolean publicar(final OutboxEntity mensagem) {
        final var correlationId = mensagem.getCorrelationId().toString();

        try (var ignored = MDC.putCloseable("correlationId", correlationId)) {
            producerService.send(mensagem.getTopico(), correlationId, mensagem.getPayload(), correlationId);
            mensagem.marcarPublicada();

            log.info("outbox message published id={} topic={}", mensagem.getId(), mensagem.getTopico());
            return true;
        } catch (RuntimeException e) {
            mensagem.registrarFalha(e.getMessage());

            log.warn("outbox message failed id={} topic={} tentativas={}",
                    mensagem.getId(), mensagem.getTopico(), mensagem.getTentativas(), e);
            return false;
        }
    }
}
