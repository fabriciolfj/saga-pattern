package com.exemplo.saga.orquestrador.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.nio.charset.StandardCharsets;

public class GetHeaderSagaId {

    private GetHeaderSagaId() { }

    public static final String HEADER_SAGA_ID = "sagaId";

    public static String getCorrelation(final ConsumerRecord<String, String> record) {
        final var header = record.headers().lastHeader(HEADER_SAGA_ID);
        return header == null ? null : new String(header.value(), StandardCharsets.UTF_8);
    }
}
