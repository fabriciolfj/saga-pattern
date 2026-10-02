package com.exemplo.saga.orquestrador.service.saga;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.repository.SagaInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.exemplo.saga.orquestrador.service.saga.SagaInstanceEntityMapper.toEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaService {

    private final SagaInstanceRepository sagaInstanceRepository;

    public void executePersist(final Saga saga) {
        final var entity = toEntity(saga);

        sagaInstanceRepository.save(entity);

        log.info("saga persisted sagaId={} etapa={} status={}", saga.getId(), saga.getEtapaAtual(), saga.getStatus());
    }
}
