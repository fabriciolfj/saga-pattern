package com.exemplo.saga.orquestrador.service.deposito;

import com.exemplo.saga.orquestrador.domain.Saga;

public class DepositMapper {

    private DepositMapper() {
    }

    public static ContratoPix.DepositarPix toDepositarPix(final Saga saga) {
        return ContratoPix.DepositarPix.builder()
                .txid(saga.getIdTransacao())
                .chavePix(saga.getDocumentCustomer())
                .valorCentavos(saga.getValue())
                .build();
    }
}
