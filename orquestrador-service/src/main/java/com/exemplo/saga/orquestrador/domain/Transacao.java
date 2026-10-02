package com.exemplo.saga.orquestrador.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;

@Builder
public record Transacao(String transactionId, BigDecimal value, String documentCustomer) {

    public Transacao {
        Objects.requireNonNull(transactionId);
        Objects.requireNonNull(value);
        Objects.requireNonNull(documentCustomer);
    }
}
