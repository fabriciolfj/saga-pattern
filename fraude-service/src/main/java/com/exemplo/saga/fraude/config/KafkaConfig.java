package com.exemplo.saga.fraude.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * Cria os dois topicos deste servico na subida, para nao depender do
 * auto-create do broker nem da ordem em que os servicos sobem.
 *
 * Produtor, consumidor e KafkaTemplate<String, String> vem da autoconfiguracao
 * do Spring Boot, a partir de spring.kafka.* no application.yml.
 */
@Configuration
@EnableConfigurationProperties(TopicosProperties.class)
public class KafkaConfig {

    private static final int PARTICOES = 3;
    private static final int REPLICAS = 1;

    @Bean
    KafkaAdmin.NewTopics topicos(TopicosProperties topicos) {
        return new KafkaAdmin.NewTopics(
                topico(topicos.entrada()),
                topico(topicos.resposta()));
    }

    private static NewTopic topico(String nome) {
        return TopicBuilder.name(nome).partitions(PARTICOES).replicas(REPLICAS).build();
    }
}
