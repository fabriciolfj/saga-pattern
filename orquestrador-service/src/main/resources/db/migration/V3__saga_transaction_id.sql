ALTER TABLE saga_instance ADD COLUMN transaction_id varchar(100);

UPDATE saga_instance SET transaction_id = dados ->> 'transactionId' WHERE transaction_id IS NULL;

ALTER TABLE saga_instance ALTER COLUMN transaction_id SET NOT NULL;

ALTER TABLE saga_instance ADD CONSTRAINT uk_saga_transaction_id UNIQUE (transaction_id);
