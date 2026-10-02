package com.exemplo.saga.cancelamento;

import com.exemplo.saga.cancelamento.config.TopicosProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Cancelamento: recebe e cancela. Nao decide nada.
 *
 * CONTRATO PROPRIO - aqui o identificador se chama "codigoTransacao", nao
 * "transacaoId". O orquestrador que faca a traducao.
 */
@Component
public class CancelamentoListener {

    private static final Logger log = LoggerFactory.getLogger(CancelamentoListener.class);

    public record CancelarTransacao(String codigoTransacao, String motivoCancelamento) {
    }

    public record TransacaoCancelada(String codigoTransacao, String canceladoEm) {
    }

    private final KafkaTemplate<String, String> kafka;
    private final TopicosProperties topicos;
    private final ObjectMapper mapper = new ObjectMapper();

    public CancelamentoListener(KafkaTemplate<String, String> kafka, TopicosProperties topicos) {
        this.kafka = kafka;
        this.topicos = topicos;
    }

    @KafkaListener(topics = "${topicos.entrada}", groupId = "cancelamento-service")
    public void cancelar(String mensagem) throws Exception {
        var pedido = mapper.readValue(mensagem, CancelarTransacao.class);

        log.info("CANCELADA a transacao {} - motivo: {}",
                pedido.codigoTransacao(), pedido.motivoCancelamento());

        var resposta = new TransacaoCancelada(pedido.codigoTransacao(), Instant.now().toString());
        kafka.send(topicos.resposta(), pedido.codigoTransacao(), mapper.writeValueAsString(resposta));
    }
}
