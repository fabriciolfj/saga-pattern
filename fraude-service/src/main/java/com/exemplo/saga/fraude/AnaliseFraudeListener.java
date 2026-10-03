package com.exemplo.saga.fraude;

import com.exemplo.saga.fraude.config.TopicosProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnaliseFraudeListener {

    private static final Logger log = LoggerFactory.getLogger(AnaliseFraudeListener.class);

    private static final String HEADER_SAGA_ID = "sagaId";

    public record AnalisarTransacao(String transacaoId, BigDecimal valor, String documentoCliente) {
    }

    public record TransacaoAnalisada(String transacaoId, boolean fraudulenta, double score) {
    }

    private final KafkaTemplate<String, String> kafka;
    private final TopicosProperties topicos;
    private final ObjectMapper mapper = new ObjectMapper();

    public AnaliseFraudeListener(KafkaTemplate<String, String> kafka, TopicosProperties topicos) {
        this.kafka = kafka;
        this.topicos = topicos;
    }

    @KafkaListener(topics = "${topicos.entrada}", groupId = "fraude-service")
    public void analisar(ConsumerRecord<String, String> comando) throws Exception {
        var pedido = mapper.readValue(comando.value(), AnalisarTransacao.class);

        boolean fraudulenta = ThreadLocalRandom.current().nextBoolean();
        double score = ThreadLocalRandom.current().nextDouble();

        log.info("transacao {} valor {} -> fraudulenta={} score={}",
                pedido.transacaoId(), pedido.valor(), fraudulenta, "%.2f".formatted(score));

        var resposta = new TransacaoAnalisada(pedido.transacaoId(), fraudulenta, score);
        var record = new ProducerRecord<>(topicos.resposta(), pedido.transacaoId(), mapper.writeValueAsString(resposta));

        // devolve o sagaId recebido do orquestrador, sem interpretar
        var sagaId = comando.headers().lastHeader(HEADER_SAGA_ID);
        if (sagaId != null) {
            record.headers().add(HEADER_SAGA_ID, sagaId.value());
        }

        kafka.send(record);
    }
}
