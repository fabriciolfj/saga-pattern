CREATE TABLE outbox (
    id             uuid         PRIMARY KEY,
    correlation_id uuid         NOT NULL,
    topico         varchar(100) NOT NULL,
    payload        jsonb        NOT NULL,
    tentativas     int          NOT NULL DEFAULT 0,
    ultimo_erro    text,
    created_at     timestamptz  NOT NULL DEFAULT now(),
    published_at   timestamptz
);

CREATE INDEX idx_outbox_pendente ON outbox (created_at) WHERE published_at IS NULL;
