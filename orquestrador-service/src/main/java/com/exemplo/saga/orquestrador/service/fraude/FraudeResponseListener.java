package com.exemplo.saga.orquestrador.service.fraude;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.exemplo.saga.orquestrador.util.GetHeaderSagaId.HEADER_SAGA_ID;
import static com.exemplo.saga.orquestrador.util.GetHeaderSagaId.getCorrelation;
import static com.exemplo.saga.orquestrador.util.JsonMapperUtil.JSON_MAPPER;

@Slf4j
@Component
@RequiredArgsConstructor
public class FraudeResponseListener {

    private final FraudeRetornoService fraudeRetornoService;

    @KafkaListener(topics = "${topicos.fraude.resposta}")
    public void onMessage(final ConsumerRecord<String, String> record) {
        final var resposta = JSON_MAPPER.readValue(record.value(), ContratoFraude.TransacaoAnalisada.class);

        final var sagaId = getCorrelation(record);

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
}
