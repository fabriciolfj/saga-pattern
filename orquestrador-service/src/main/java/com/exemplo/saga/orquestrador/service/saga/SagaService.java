package com.exemplo.saga.orquestrador.service.saga;

import com.exemplo.saga.orquestrador.domain.Saga;
import com.exemplo.saga.orquestrador.repository.SagaInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    public Saga findSaga(final String id) {
        return sagaInstanceRepository.findById(UUID.fromString(id))
                .map(SagaInstanceEntityMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Saga not found " + id));
    }

    @Transactional
    public void update(final Saga saga) {
        final var entity = sagaInstanceRepository.findById(UUID.fromString(saga.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Saga not found " + saga.getId()));

        if (!entity.getVersion().equals(saga.getVersion())) {
            throw new OptimisticLockingFailureException("saga " + saga.getId() + " changed: version "
                    + saga.getVersion() + " read, " + entity.getVersion() + " in database");
        }

        final var etapaAnterior = entity.getEtapa();
        entity.setEtapa(saga.getEtapaAtual());
        entity.setStatus(saga.getStatus());

        log.info("saga updated sagaId={} etapa={} -> {} status={}",
                saga.getId(), etapaAnterior, saga.getEtapaAtual(), saga.getStatus());
    }

    public Saga findSagaByTransactionId(final String transactionId) {
        return sagaInstanceRepository.findByTransactionId(transactionId)
                .map(SagaInstanceEntityMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Saga not found for transactionId " + transactionId));
    }
}
