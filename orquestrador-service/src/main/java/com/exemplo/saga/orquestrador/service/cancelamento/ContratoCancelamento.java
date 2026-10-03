package com.exemplo.saga.orquestrador.service.cancelamento;

import lombok.Builder;

public final class ContratoCancelamento {

    @Builder
    public record CancelarTransacao(String codigoTransacao, String motivoCancelamento) {
    }

    @Builder
    public record TransacaoCancelada(String codigoTransacao, String canceladoEm) {
    }

    private ContratoCancelamento() {
    }
}
