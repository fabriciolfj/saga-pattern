package com.exemplo.saga.orquestrador.service.deposito;

/** Contrato do pix: id e txid e o valor vem em CENTAVOS (long), nao BigDecimal. */
public final class ContratoPix {

    public record DepositarPix(String txid, String chavePix, long valorCentavos) {
    }

    public record PixDepositado(String txid, String status, String comprovante) {
    }

    private ContratoPix() {
    }
}
