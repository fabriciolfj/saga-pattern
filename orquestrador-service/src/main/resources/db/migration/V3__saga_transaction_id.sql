-- transaction_id é o id de negócio da transação (vem do POST e trafega nos comandos);
-- saga_id é só a chave da saga. As respostas dos participantes trazem o transaction_id,
-- então é por ele que o orquestrador encontra a saga.
ALTER TABLE saga_instance ADD COLUMN transaction_id varchar(100);

UPDATE saga_instance SET transaction_id = dados ->> 'transactionId' WHERE transaction_id IS NULL;

ALTER TABLE saga_instance ALTER COLUMN transaction_id SET NOT NULL;

-- uma saga por transação: também impede que o mesmo POST repetido abra outra saga
ALTER TABLE saga_instance ADD CONSTRAINT uk_saga_transaction_id UNIQUE (transaction_id);
