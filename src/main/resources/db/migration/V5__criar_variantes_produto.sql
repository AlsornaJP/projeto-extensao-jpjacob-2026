CREATE TABLE variantes (
    id SERIAL PRIMARY KEY,
    id_produto INT NOT NULL REFERENCES produtos(id),
    medida     VARCHAR(50) NOT NULL,
    preco      NUMERIC(10,2) NOT NULL,
    estoque    INT NOT NULL DEFAULT 0,
    CONSTRAINT ck_variantes_estoque_nao_negativo CHECK (estoque >= 0),
    CONSTRAINT uq_variante_medida_por_produto UNIQUE (id_produto, medida)
);

INSERT INTO variantes (id_produto, medida, preco, estoque)
SELECT id, 'Unica', preco, estoque FROM produtos;

ALTER TABLE itens ADD COLUMN id_variante INT REFERENCES variantes(id);

UPDATE itens i
SET id_variante = v.id
FROM variantes v
WHERE v.id_produto = i.id_produto;

ALTER TABLE itens
    ALTER COLUMN id_variante SET NOT NULL,
    DROP COLUMN id_produto;

ALTER TABLE produtos
    DROP COLUMN preco,
    DROP COLUMN estoque;
