CREATE TABLE enderecos (
    id SERIAL PRIMARY KEY,
    rua VARCHAR(255) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    numero INTEGER NOT NULL,
    complemento VARCHAR(255),
    cep VARCHAR(50) NOT NULL
    );

CREATE TABLE carrinhos (
    id SERIAL PRIMARY KEY ,
    preco_total NUMERIC(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE fornecedores (
    id SERIAL PRIMARY KEY,
    nome_fornecedor VARCHAR(150) NOT NULL
);

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome_produto VARCHAR(150) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    id_fornecedor INT NOT NULL REFERENCES fornecedores(id)
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome_cliente  VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    telefone      VARCHAR(20)  NOT NULL UNIQUE,
    id_endereco   INT NOT NULL REFERENCES enderecos(id),
    id_carrinho   INT NOT NULL REFERENCES carrinhos(id)
);

CREATE TABLE itens (
    id SERIAL PRIMARY KEY,
    quantidade   INT NOT NULL,
    preco        NUMERIC(10,2) NOT NULL,
    id_produto   INT NOT NULL REFERENCES produtos(id),
    id_carrinho  INT NOT NULL REFERENCES carrinhos(id)

);

CREATE TABLE pedidos (
    id SERIAL PRIMARY KEY,
    data_pedido  DATE NOT NULL,
    hora_pedido  TIME NOT NULL,
    id_cliente   INT NOT NULL REFERENCES clientes(id),
    id_carrinho  INT NOT NULL REFERENCES carrinhos(id)

);

CREATE TABLE entregas (
    id SERIAL PRIMARY KEY,
    id_pedido       INT NOT NULL REFERENCES pedidos(id),
    id_endereco     INT NOT NULL REFERENCES enderecos(id),
    data_postagem   DATE NOT NULL,
    status          VARCHAR(50) NOT NULL
);