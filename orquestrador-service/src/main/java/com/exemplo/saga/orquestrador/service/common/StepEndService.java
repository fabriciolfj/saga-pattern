package com.exemplo.saga.orquestrador.service.common;

import com.exemplo.saga.orquestrador.domain.Saga;

public interface StepEndService {

    void execute(final Saga saga);
}
