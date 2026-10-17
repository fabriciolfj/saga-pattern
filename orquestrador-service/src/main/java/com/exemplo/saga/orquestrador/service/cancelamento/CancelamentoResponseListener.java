package com.exemplo.saga.orquestrador.service.cancelamento;

import com.exemplo.saga.orquestrador.util.GetHeaderSagaId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.exemplo.saga.orquestrador.util.GetHeaderSagaId.HEADER_SAGA_ID;
import static com.exemplo.saga.orquestrador.util.JsonMapperUtil.JSON_MAPPER;


@Slf4j
@Service
@RequiredArgsConstructor
public class CancelamentoResponseListener {

    private final CancelamentoResponseService cancelamentoResponseService;

    @KafkaListener(topics = "${topicos.cancelamento.resposta}")
    public void receive(final ConsumerRecord<String, String> record) {
        var dto = JSON_MAPPER.readValue(record.value(), ContratoCancelamento.TransacaoCancelada.class);

        var sagaId = GetHeaderSagaId.getCorrelation(record);

        try (var ignored = MDC.putCloseable("correlationId", sagaId)) {
            if (sagaId == null) {
                log.warn("cancel response without header {} correlationId partition={} offset={}",
                        HEADER_SAGA_ID, record.partition(), record.offset());
                return;
            }

            cancelamentoResponseService.execute(sagaId, dto);
        }
    }
}
