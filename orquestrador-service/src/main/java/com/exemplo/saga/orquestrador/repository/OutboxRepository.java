package com.exemplo.saga.orquestrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {

    @Query(value = """
            SELECT * FROM outbox
             WHERE published_at IS NULL
             ORDER BY created_at
             LIMIT :limite
               FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEntity> buscarPendentesParaPublicar(@Param("limite") int limite);
}
