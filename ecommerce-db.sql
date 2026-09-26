CREATE DATABASE ecommerce_db;

USE ecommerce_db;

CREATE TABLE tb_usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(255) NOT NULL,
    email_usuario VARCHAR(255) NOT NULL UNIQUE,
    senha_usuario VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('cliente', 'administrador') NOT NULL DEFAULT 'cliente',
    data_cadastro DATE NOT NULL
);

CREATE TABLE tb_produtos(
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(80) NOT NULL,
    descricao_produto VARCHAR(255),
    plataforma ENUM(
    "steam", 
    "xbox", 
    "playstation", 
    "nintendo_switch",
    "serviços_e_entretenimento"
    ) NOT NULL,
    valor_produto DECIMAL(10,2) NOT NULL
);

CREATE TABLE tb_pedidos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    status ENUM(
		'pendente',
        'pago',
        'cancelado'
    )NOT NULL DEFAULT 'pendente',
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
    id_item_pedido INT UNIQUE NOT NULL,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    status ENUM(
		'disponivel',
        'vendido'
    ) NOT NULL DEFAULT 'disponivel',
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
SET tipo_usuario = 'administrador'
WHERE email_usuario = 'joao@gmail.com' and "eduardo@gmail.com" and "carlos@gmail.com";

SELECT * FROM tb_usuarios;
SELECT * FROM tb_produtos;
SELECT * FROM tb_pedidos;
SELECT * FROM tb_itens_pedido;
SELECT * FROM tb_codigos_giftcard;
SELECT * FROM tb_tokens_recuperacao;