package com.exemplo.saga.orquestrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {

    /**
     * Trava as linhas lidas até o fim da transação. SKIP LOCKED faz outra instância do
     * orquestrador pular o que já está sendo publicado em vez de esperar ou duplicar.
     */
    @Query(value = """
            SELECT * FROM outbox
             WHERE published_at IS NULL
             ORDER BY created_at
             LIMIT :limite
               FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEntity> buscarPendentesParaPublicar(@Param("limite") int limite);
}
