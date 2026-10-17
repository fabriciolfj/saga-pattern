package com.exemplo.saga.cancelamento.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;


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
