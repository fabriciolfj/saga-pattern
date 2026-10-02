package com.exemplo.saga.orquestrador.service.fraude;

import lombok.Builder;

import java.math.BigDecimal;


public final class ContratoFraude {

    @Builder
    public record AnalisarTransacao(String transacaoId, BigDecimal valor, String documentoCliente) {
    }

    @Builder
    public record TransacaoAnalisada(String transacaoId, boolean fraudulenta, double score) {
    }

    private ContratoFraude() {
    }
}
