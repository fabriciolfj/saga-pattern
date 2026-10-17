package com.exemplo.saga.cancelamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("topicos")
public record TopicosProperties(String entrada, String resposta) {
}
