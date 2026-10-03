package com.exemplo.saga.orquestrador.service.deposito;

import com.exemplo.saga.orquestrador.util.GetHeaderSagaId;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.exemplo.saga.orquestrador.util.GetHeaderSagaId.HEADER_SAGA_ID;
import static com.exemplo.saga.orquestrador.util.JsonMapperUtil.JSON_MAPPER;

@Service
@Slf4j
public class DepositoResponseListener {

    @KafkaListener(topics = "${topicos.pix.resposta}")
    public void receive(final ConsumerRecord<String, String> record) {
        var resposta = JSON_MAPPER.readValue(record.value(), ContratoPix.PixDepositado.class);

        var sagaId = GetHeaderSagaId.getCorrelation(record);
        try(var ignored = MDC.putCloseable("correlationId" , GetHeaderSagaId.getCorrelation(record))) {
            if (sagaId == null) {
                log.warn("deposit response without {} header transactionId={} partition={} offset={}",
                        HEADER_SAGA_ID, resposta.txid(), record.partition(), record.offset());
            }

            log.info("deposit response received sagaId={} transactionId={} partition={} offset={}",
                    sagaId, resposta.txid(), record.partition(), record.offset());
        }

    }
}
