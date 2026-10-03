package com.exemplo.saga.orquestrador.repository;

import com.exemplo.saga.orquestrador.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SagaInstanceRepository extends JpaRepository<SagaInstanceEntity, UUID> {

    Optional<SagaInstanceEntity> findByTransactionId(String transactionId);

    List<SagaInstanceEntity> findByStatusAndUpdatedAtBeforeOrderByUpdatedAt(Status status, OffsetDateTime limite);
}
