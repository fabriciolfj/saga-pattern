package com.exemplo.saga.orquestrador.kafka;

public class PublicacaoKafkaException extends RuntimeException {

    public PublicacaoKafkaException(final String topic, final Throwable cause) {
        super("failed to publish to topic " + topic + ": " + cause.getMessage(), cause);
    }
}
