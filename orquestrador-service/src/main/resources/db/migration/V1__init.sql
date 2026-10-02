CREATE TABLE saga_instance (
    saga_id    uuid        PRIMARY KEY,
    etapa      varchar(40) NOT NULL,
    status     varchar(20) NOT NULL,   -- EXECUTANDO | CONCLUIDA | ABORTADA
    dados      jsonb       NOT NULL,
    version    int         NOT NULL DEFAULT 0,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);


CREATE INDEX idx_saga_execucao ON saga_instance (status, updated_at);
