CREATE DATABASE IF NOT EXISTS ecommerce_db;

USE ecommerce_db;

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(255) NOT NULL,
    email_usuario VARCHAR(255) NOT NULL UNIQUE,
    senha_usuario VARCHAR(255) NOT NULL,
    
    tipo_usuario ENUM(
		'Cliente', 
        'Administrador'
	) NOT NULL DEFAULT 'Cliente',
    
    data_cadastro DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_produtos (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(80) NOT NULL,
    descricao_produto VARCHAR(255),
    
    plataforma ENUM(
		'Steam', 
		'Xbox', 
		'Playstation', 
		'Nintendo'
    ) NOT NULL,
    
    valor_produto DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_pedidos (
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    
    status ENUM(
		'Pendente',
        'Pago',
        'Cancelado'
    ) NOT NULL DEFAULT 'Pendente',
    
    data_pedido DATETIME NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    
    FOREIGN KEY (id_usuario) 
		REFERENCES tb_usuarios(id),
    
    INDEX idx_pedidos_status_data (status, data_pedido)
);

CREATE TABLE IF NOT EXISTS tb_itens_pedido (
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    id_pedido INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    
    FOREIGN KEY (id_produto) 
		REFERENCES tb_produtos(id),
    
    FOREIGN KEY (id_pedido) 
		REFERENCES tb_pedidos(id)
);

CREATE TABLE IF NOT EXISTS tb_pagamentos (
    id_pagamento INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT NOT NULL,
    id_pagamento_asaas VARCHAR(100) UNIQUE,
    valor DECIMAL(12,2) NOT NULL,
    
    forma_pagamento ENUM(
		'PIX',
        'CREDIT_CARD'
    ) NOT NULL,
    
    status ENUM(
        'PENDING',
        'CONFIRMED',
        'RECEIVED',
        'OVERDUE',
        'REFUNDED',
        'CANCELLED'
    ) NOT NULL DEFAULT 'PENDING',
    
    data_criacao DATETIME NOT NULL,
    data_confirmacao DATETIME,

    FOREIGN KEY (id_pedido) 
		REFERENCES tb_pedidos(id)
);

CREATE TABLE IF NOT EXISTS tb_eventos_webhook(
	id_evento_asaas VARCHAR(100) PRIMARY KEY,
    tipo_evento VARCHAR(80) NOT NULL,
    id_pagamento_asaas VARCHAR(100) NOT NULL,
    payload LONGTEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'RECEBIDO',
    data_recebimento DATETIME NOT NULL,
    data_processamento DATETIME,
    
    INDEX idx_eventos_pagamento (id_pagamento_asaas),
    
    INDEX idx_eventos_status (status)
);

CREATE TABLE IF NOT EXISTS tb_codigos_giftcard(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    id_item_pedido INT UNIQUE,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    
    status ENUM(
		'Disponivel',
        'Vendido'
    ) NOT NULL DEFAULT 'Disponivel',
    
    FOREIGN KEY (id_produto) 
		REFERENCES tb_produtos(id),
    
    FOREIGN KEY (id_item_pedido) 
		REFERENCES tb_itens_pedido(id)
);

CREATE TABLE IF NOT EXISTS tb_tokens_recuperacao(
	id INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    data_expiracao DATETIME NOT NULL,
    utilizado BOOLEAN NOT NULL DEFAULT FALSE,
    
    FOREIGN KEY (id_usuario) 
		REFERENCES tb_usuarios(id)
);