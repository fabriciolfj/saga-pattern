package com.exemplo.saga.orquestrador.service.deposito;

import com.exemplo.saga.orquestrador.domain.Etapa;
import com.exemplo.saga.orquestrador.service.saga.SagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DepositoResponseService {

    private final SagaService sagaService;

    public void execute(final String sagaId, final ContratoPix.PixDepositado deposito) {
        var saga = sagaId == null ? sagaService.findSagaByTransactionId(deposito.txid())
                 : sagaService.findSaga(sagaId);

        if (!saga.isAwaiting(Etapa.DEPOSITO)) {
            log.warn("step invalid sagaId {}", sagaId);
            return;
        }

        saga.doneStepDeposit();
        sagaService.update(saga);
        log.info("finished step to saga {}", saga.getId());
    }
}
