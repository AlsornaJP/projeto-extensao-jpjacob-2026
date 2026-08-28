--Rodar manualmente via terminal

-- 1. Carrinhos (um por cliente, comecam vazios/zerados)
INSERT INTO carrinhos (preco_total) VALUES
(0),
(0),
(0);

-- 2. Fornecedores
INSERT INTO fornecedores (nome_fornecedor) VALUES
('Laticinios Serra Mineira'),
('Alambique Sao Roque'),
('Doceria da Roca');

-- 3. Produtos (cada um com fornecedor obrigatorio)
INSERT INTO produtos (nome_produto, descricao, preco, id_fornecedor) VALUES
('Queijo Minas Artesanal 500g', 'Queijo curado tradicional da regiao de Tiradentes', 32.90, 1),
('Cachaca Envelhecida 700ml', 'Cachaca artesanal envelhecida em barril de carvalho', 65.00, 2),
('Doce de Leite Cremoso 300g', 'Doce de leite pastoso feito em tacho de cobre', 18.50, 3),
('Linguica Artesanal Defumada 1kg', 'Linguica suina temperada e defumada na lenha', 42.00, 1);

-- 4. Clientes (cada um com carrinho proprio)
INSERT INTO clientes (nome_cliente, email, telefone, id_carrinho) VALUES
('Mariana Costa', 'mariana.costa@email.com', '32988001122', 1),
('Rafael Andrade', 'rafael.andrade@email.com', '32999223344', 2),
('Juliana Pires', 'juliana.pires@email.com', '31987654321', 3);

-- 5. Enderecos ("ativo" usa o DEFAULT true,
--    entao nao precisa ser informado aqui)
INSERT INTO enderecos (id_cliente, rua, bairro, cidade, estado, numero, complemento, cep) VALUES
(1, 'Rua das Cerejeiras', 'Centro', 'Tiradentes', 'MG', 120, 'Casa 2', '36325000'),
(2, 'Av. Getulio Vargas', 'Sao Jose', 'Sao Joao del-Rei', 'MG', 845, NULL, '36301000'),
(3, 'Rua do Rosario', 'Praca', 'Ouro Preto', 'MG', 45, 'Fundos', '35400000');

-- 6. Itens (produtos dentro dos carrinhos)
INSERT INTO itens (quantidade, preco, id_produto, id_carrinho) VALUES
(2, 32.90, 1, 1),   -- Mariana: 2x Queijo Minas
(1, 65.00, 2, 1),   -- Mariana: 1x Cachaca
(3, 18.50, 3, 2),   -- Rafael: 3x Doce de Leite
(1, 42.00, 4, 3);   -- Juliana: 1x Linguica

-- Atualiza o preco_total dos carrinhos de acordo com os itens inseridos
UPDATE carrinhos SET preco_total = 130.80 WHERE id = 1;  -- (2*32.90)+(1*65.00)
UPDATE carrinhos SET preco_total = 55.50  WHERE id = 2;  -- (3*18.50)
UPDATE carrinhos SET preco_total = 42.00  WHERE id = 3;  -- (1*42.00)

-- 7. Pedidos (um pedido fechado por cliente, a partir do carrinho)
INSERT INTO pedidos (data_pedido, hora_pedido, id_cliente, id_carrinho) VALUES
('2026-08-05', '14:32:00', 1, 1),
('2026-08-06', '09:15:00', 2, 2),
('2026-08-06', '18:47:00', 3, 3);

-- 8. Entregas (uma por pedido, com status variados para testar o fluxo)
INSERT INTO entregas (id_pedido, id_endereco, data_postagem, status) VALUES
(1, 1, '2026-08-06', 'ENTREGUE'),
(2, 2, '2026-08-07', 'PAGO'),
(3, 3, '2026-08-07', 'PENDENTE');
