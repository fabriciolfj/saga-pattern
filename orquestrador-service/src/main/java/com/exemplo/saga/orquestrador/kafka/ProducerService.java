package com.exemplo.saga.orquestrador.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.exemplo.saga.orquestrador.util.GetHeaderSagaId.HEADER_SAGA_ID;

@Slf4j
@Component
public class ProducerService {

    private static final long TIMEOUT_SEGUNDOS = 15;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final String topic, final String key, final String payload, final String sagaId) {
        final var record = new ProducerRecord<>(topic, key, payload);
        record.headers().add(HEADER_SAGA_ID, sagaId.getBytes(StandardCharsets.UTF_8));

        try {
            final var metadata = kafkaTemplate.send(record)
                    .get(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)
                    .getRecordMetadata();

            log.info("message delivered topic={} partition={} offset={} sagaId={}",
                    metadata.topic(), metadata.partition(), metadata.offset(), sagaId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PublicacaoKafkaException(topic, e);
        } catch (ExecutionException | TimeoutException e) {
            throw new PublicacaoKafkaException(topic, e);
        }
    }
}
