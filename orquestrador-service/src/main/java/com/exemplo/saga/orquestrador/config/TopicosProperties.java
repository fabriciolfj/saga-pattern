package com.exemplo.saga.orquestrador.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("topicos")
public record TopicosProperties(Participante fraude, Participante cancelamento, Participante pix) {


    public record Participante(String entrada, String resposta) {
    }
}
