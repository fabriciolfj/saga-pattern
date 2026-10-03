package com.exemplo.saga.orquestrador.service.deposito;


import lombok.Builder;

import java.math.BigDecimal;


public final class ContratoPix {

    @Builder
    public record DepositarPix(String txid, String chavePix, BigDecimal valorCentavos) {
    }

    @Builder
    public record PixDepositado(String txid, String status, String comprovante) {
    }

    private ContratoPix() {
    }
}
