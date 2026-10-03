package com.exemplo.saga.orquestrador.service.fraude;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

import static com.exemplo.saga.orquestrador.kafka.ProducerService.HEADER_SAGA_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FraudeResponseListener {

    private final JsonMapper jsonMapper;
    private final FraudeRetornoService fraudeRetornoService;

    @KafkaListener(topics = "${topicos.fraude.resposta}")
    public void onMessage(final ConsumerRecord<String, String> record) {
        final var resposta = jsonMapper.readValue(record.value(), ContratoFraude.TransacaoAnalisada.class);
        final var sagaId = sagaIdDoHeader(record);

        try (var ignored = MDC.putCloseable("correlationId", sagaId)) {
            if (sagaId == null) {
                log.warn("fraud response without {} header transactionId={} partition={} offset={}",
                        HEADER_SAGA_ID, resposta.transacaoId(), record.partition(), record.offset());
            }

            log.info("fraud response received sagaId={} transactionId={} fraudulenta={} score={} partition={} offset={}",
                    sagaId, resposta.transacaoId(), resposta.fraudulenta(), "%.2f".formatted(resposta.score()),
                    record.partition(), record.offset());

            fraudeRetornoService.processFraudeRetorno(sagaId, resposta);
        }
    }

    private static String sagaIdDoHeader(final ConsumerRecord<String, String> record) {
        final var header = record.headers().lastHeader(HEADER_SAGA_ID);
        return header == null ? null : new String(header.value(), StandardCharsets.UTF_8);
    }
}
