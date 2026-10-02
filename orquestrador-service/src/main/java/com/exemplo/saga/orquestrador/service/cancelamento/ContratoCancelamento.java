package com.exemplo.saga.orquestrador.service.cancelamento;

public final class ContratoCancelamento {

    public record CancelarTransacao(String codigoTransacao, String motivoCancelamento) {
    }

    public record TransacaoCancelada(String codigoTransacao, String canceladoEm) {
    }

    private ContratoCancelamento() {
    }
}
