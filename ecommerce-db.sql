CREATE DATABASE ecommerce_db;

USE ecommerce_db;

CREATE TABLE tb_usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(255) NOT NULL,
    email_usuario VARCHAR(255) NOT NULL UNIQUE,
    senha_usuario VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('Cliente', 'Administrador') NOT NULL DEFAULT 'Cliente',
    data_cadastro DATE NOT NULL
);

CREATE TABLE tb_produtos(
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(80) NOT NULL,
    descricao_produto VARCHAR(255),
    plataforma ENUM(
    "Steam", 
    "Xbox", 
    "Playstation", 
    "Nintendo"
    ) NOT NULL,
    valor_produto DECIMAL(10,2) NOT NULL
);

CREATE TABLE tb_pedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    status ENUM(
		'Pendente',
        'Pago',
        'Cancelado'
    )NOT NULL DEFAULT 'Pendente',
    data_pedido DATETIME NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY(id_usuario) REFERENCES tb_usuarios(id)
);

CREATE TABLE tb_itens_pedido(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    id_pedido INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY(id_produto) REFERENCES tb_produtos(id),
    FOREIGN KEY(id_pedido) REFERENCES tb_pedidos(id)
);

CREATE TABLE tb_codigos_giftcard(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    id_item_pedido INT UNIQUE,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    status ENUM(
		'Disponivel',
        'Vendido'
    ) NOT NULL DEFAULT 'Disponivel',
    FOREIGN KEY(id_produto) REFERENCES tb_produtos(id),
    FOREIGN KEY(id_item_pedido) REFERENCES tb_itens_pedido(id)
);

CREATE TABLE tb_tokens_recuperacao(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    data_expiracao DATETIME NOT NULL,
    utilizado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (id_usuario) REFERENCES tb_usuarios(id)
);

DESC tb_tokens_recuperacao;

UPDATE tb_usuarios
SET tipo_usuario = 'Administrador'
WHERE email_usuario = 'joao@gmail.com';

INSERT INTO tb_produtos (id, nome_produto, descricao_produto, plataforma, valor_produto)
VALUES 
(DEFAULT, 'Gift Card Nintendo Switch R$ 250', 'Gift card digital para Nintendo Switch', 'Nintendo', 250),
(DEFAULT, 'Gift Card Xbox R$ 100', 'Gift card digital para Xbox', 'Xbox', 100),
(DEFAULT, 'Gift Card Xbox R$ 150', 'Gift card digital para Xbox', 'Xbox', 150),
(DEFAULT, 'Gift Card Nintendo Switch R$ 50', 'Gift card digital para Nintendo Switch', 'Nintendo', 50),
(DEFAULT, 'Gift Card Playstation R$ 50', 'Gift card digital para Playstation', 'Playstation', 50),
(DEFAULT, 'Gift Card Playstation R$ 100', 'Gift card digital para Playstation', 'Playstation', 100),
(DEFAULT, 'Gift Card Nintendo Switch R$ 100', 'Gift card digital para Nintendo Switch', 'Nintendo', 100);

SELECT * FROM tb_usuarios;
SELECT * FROM tb_produtos;
SELECT * FROM tb_pedidos;
SELECT * FROM tb_itens_pedido;
SELECT * FROM tb_codigos_giftcard;
SELECT * FROM tb_tokens_recuperacao;