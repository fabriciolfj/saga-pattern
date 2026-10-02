package com.exemplo.saga.cancelamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Nomes dos topicos deste servico, lidos de topicos.* no application.yml.
 *
 * @param entrada  onde o servico recebe o comando
 * @param resposta onde o servico publica o resultado
 */
@ConfigurationProperties("topicos")
public record TopicosProperties(String entrada, String resposta) {
}
