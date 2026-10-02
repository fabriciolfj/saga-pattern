package com.exemplo.saga.pix;

import com.exemplo.saga.pix.config.TopicosProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * PIX: recebe e deposita (um print, por enquanto).
 *
 * CONTRATO PROPRIO - aqui o identificador e "txid", o valor vem em CENTAVOS
 * (long) e ha uma chave pix. Tres divergencias em relacao ao antifraude, que
 * usa transacaoId e BigDecimal.
 */
@Component
public class DepositoPixListener {

    private static final Logger log = LoggerFactory.getLogger(DepositoPixListener.class);

    public record DepositarPix(String txid, String chavePix, long valorCentavos) {
    }

    public record PixDepositado(String txid, String status, String comprovante) {
    }

    private final KafkaTemplate<String, String> kafka;
    private final TopicosProperties topicos;
    private final ObjectMapper mapper = new ObjectMapper();

    public DepositoPixListener(KafkaTemplate<String, String> kafka, TopicosProperties topicos) {
        this.kafka = kafka;
        this.topicos = topicos;
    }

    @KafkaListener(topics = "${topicos.entrada}", groupId = "pix-service")
    public void depositar(String mensagem) throws Exception {
        var pedido = mapper.readValue(mensagem, DepositarPix.class);

        String comprovante = "E" + System.currentTimeMillis();
        log.info("DEPOSITADO R$ {} na chave {} (txid {}) - comprovante {}",
                "%.2f".formatted(pedido.valorCentavos() / 100.0),
                pedido.chavePix(), pedido.txid(), comprovante);

        var resposta = new PixDepositado(pedido.txid(), "DEPOSITADO", comprovante);
        kafka.send(topicos.resposta(), pedido.txid(), mapper.writeValueAsString(resposta));
    }
}
