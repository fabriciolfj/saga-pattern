package com.exemplo.saga.orquestrador.service.cancelamento;

import com.exemplo.saga.orquestrador.domain.Etapa;
import com.exemplo.saga.orquestrador.service.saga.SagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelamentoResponseService {

    private final SagaService sagaService;

    @Transactional
    public void execute(final String sagaId, final ContratoCancelamento.TransacaoCancelada transacaoCancelada) {
        final var entity = sagaId == null
                ? sagaService.findSagaByTransactionId(transacaoCancelada.codigoTransacao()) :
                sagaService.findSaga(sagaId);

        if(!entity.isAwaiting(Etapa.CANCELAMENTO)) {
            log.warn("step invalid sagaId {}", sagaId);
            return;
        }

        entity.doneStepCancel();
        sagaService.update(entity);

        log.info("done step cancel do transactionId {}", entity.getIdTransacao());
    }
}
