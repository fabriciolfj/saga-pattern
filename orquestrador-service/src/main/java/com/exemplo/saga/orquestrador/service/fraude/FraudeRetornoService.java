package com.exemplo.saga.orquestrador.service.fraude;

import com.exemplo.saga.orquestrador.domain.Etapa;
import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.service.common.StepEndService;
import com.exemplo.saga.orquestrador.service.saga.SagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudeRetornoService {

    private final SagaService sagaService;
    private final Map<String, StepEndService> stepEndServiceMap;

    @Transactional
    public void processFraudeRetorno(final String sagaId, final ContratoFraude.TransacaoAnalisada transacaoAnalisada) {
        final Saga saga = sagaId != null
                ? sagaService.findSaga(sagaId)
                : sagaService.findSagaByTransactionId(transacaoAnalisada.transacaoId());

        if (!saga.isAwaiting(Etapa.FRAUDE)) {
            log.warn("fraud response ignored sagaId={} etapa={} status={}",
                    saga.getId(), saga.getEtapaAtual(), saga.getStatus());
            return;
        }

        final Etapa proximaEtapa = saga.analyseFraud(transacaoAnalisada.fraudulenta());
        sagaService.update(saga);

        log.info("next step sagaId={} fraudulenta={} proximaEtapa={}",
                saga.getId(), transacaoAnalisada.fraudulenta(), proximaEtapa);

        final StepEndService stepEndService = stepEndServiceMap.get(proximaEtapa.getDescricao());
        if (Objects.isNull(stepEndService)) {
            log.error("StepEndService not found for etapa={}", proximaEtapa.getDescricao());
            return;
        }

        stepEndService.execute(saga);
    }
}
