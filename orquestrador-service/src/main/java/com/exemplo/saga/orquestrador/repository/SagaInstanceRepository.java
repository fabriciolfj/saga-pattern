package com.exemplo.saga.orquestrador.repository;

import com.exemplo.saga.orquestrador.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface SagaInstanceRepository extends JpaRepository<SagaInstanceEntity, UUID> {

    /** Sagas num status sem atualização desde {@code limite}; usa o índice idx_saga_execucao. */
    List<SagaInstanceEntity> findByStatusAndUpdatedAtBeforeOrderByUpdatedAt(Status status, OffsetDateTime limite);
}
