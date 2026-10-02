package com.exemplo.saga.orquestrador.controller;

import com.exemplo.saga.orquestrador.domain.Status;

public record TransacaoResponse(String sagaId, Status status) {
}
