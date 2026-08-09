ALTER TABLE enderecos ADD COLUMN id_cliente INT;

UPDATE enderecos e
SET id_cliente = c.id
FROM clientes c
WHERE c.id_endereco = e.id;

ALTER TABLE enderecos
    ALTER COLUMN id_cliente SET NOT NULL,
    ADD CONSTRAINT fk_endereco_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id);

ALTER TABLE enderecos ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT true;

CREATE UNIQUE INDEX uq_endereco_ativo_por_cliente
    ON enderecos (id_cliente)
    WHERE ativo = true;

ALTER TABLE clientes DROP COLUMN id_endereco;
