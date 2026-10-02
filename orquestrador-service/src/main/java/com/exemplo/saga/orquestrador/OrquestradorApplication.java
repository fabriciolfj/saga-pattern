package com.exemplo.saga.orquestrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OrquestradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrquestradorApplication.class, args);
    }
}
