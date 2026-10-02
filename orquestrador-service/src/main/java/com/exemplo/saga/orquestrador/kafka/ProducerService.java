package com.exemplo.saga.orquestrador.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ProducerService {

    public static final String HEADER_CORRELATION_ID = "correlationId";

    // acima do delivery.timeout.ms (10s): quem desiste primeiro é o producer, com o erro real
    private static final long TIMEOUT_SEGUNDOS = 15;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envio síncrono: a outbox só pode marcar a mensagem como publicada depois do ack do broker.
     * A key é o correlationId, então as mensagens de uma mesma saga caem na mesma partição, em ordem.
     */
    public void send(final String topic, final String key, final String payload, final String correlationId) {
        final var record = new ProducerRecord<>(topic, key, payload);
        record.headers().add(HEADER_CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));

        try {
            final var metadata = kafkaTemplate.send(record)
                    .get(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)
                    .getRecordMetadata();

            log.info("message delivered topic={} partition={} offset={} key={}",
                    metadata.topic(), metadata.partition(), metadata.offset(), key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PublicacaoKafkaException(topic, e);
        } catch (ExecutionException | TimeoutException e) {
            throw new PublicacaoKafkaException(topic, e);
        }
    }
}
