-- Estoque do produto: necessario para a baixa de estoque no fechamento do pedido.
-- DEFAULT 0 preenche as linhas existentes; o CHECK impede estoque negativo no banco,
-- independentemente do que a aplicacao fizer.
ALTER TABLE produtos ADD COLUMN estoque INT NOT NULL DEFAULT 0;

ALTER TABLE produtos ADD CONSTRAINT ck_produtos_estoque_nao_negativo CHECK (estoque >= 0);
