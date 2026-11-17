USE ecommerce_db;

/* DADOS INICIAIS do banco de dados */

/* ADMINISTRADOR INICIAL */

INSERT INTO tb_usuarios (
    nome_usuario,
    email_usuario,
    senha_usuario,
    tipo_usuario,
    data_cadastro
)
VALUES (
    'Administrador',
    'admin@gmail.com',
    '$2a$10$nquxRv.rHy11JssRsKa09eZ40LAYHIf10DGZ0rZEEcSBvfrh11006',
    'Administrador',
    CURRENT_DATE()
);

/* PRODUTOS INICIAIS */

-- Steam
INSERT INTO tb_produtos (
    nome_produto,
    descricao_produto,
    plataforma,
    valor_produto
)
VALUES(
    'Gift Card Steam R$ 50',
    'Gift card digital de R$ 50 para Steam',
    'Steam',
    50.00
),
(
    'Gift Card Steam R$ 100',
    'Gift card digital de R$ 100 para Steam',
    'Steam',
    100.00
);

-- Xbox
INSERT INTO tb_produtos (
    nome_produto,
    descricao_produto,
    plataforma,
    valor_produto
)
VALUES(
    'Gift Card Xbox R$ 100',
    'Gift card digital de R$ 100 para Xbox',
    'Xbox',
    100.00
),
(
    'Gift Card Xbox R$ 150',
    'Gift card digital de R$ 150 para Xbox',
    'Xbox',
    150.00
);

-- Playstation
INSERT INTO tb_produtos (
    nome_produto,
    descricao_produto,
    plataforma,
    valor_produto
)
VALUES(
    'Gift Card Playstation R$ 50',
    'Gift card digital de R$ 50 para Playstation',
    'Playstation',
    50.00
),
(
    'Gift Card Playstation R$ 100',
    'Gift card digital de R$ 100 para Playstation',
    'Playstation',
    100.00
);


-- Nintendo
INSERT INTO tb_produtos (
    nome_produto,
    descricao_produto,
    plataforma,
    valor_produto
)
VALUES(
    'Gift Card Nintendo Switch R$ 100',
    'Gift card digital de R$ 100 para Nintendo Switch',
    'Nintendo',
    100.00
),
(
    'Gift Card Nintendo Switch R$ 250',
    'Gift card digital de R$ 250 para Nintendo Switch',
    'Nintendo',
    250.00
);

/* ESTOQUE INICIAL de GIFT CARDS */
INSERT INTO tb_codigos_giftcard (
    id_produto,
    id_item_pedido,
    codigo,
    status
)
SELECT
    p.id,
    NULL,
    dados.codigo,
    'Disponivel'
FROM (
    -- Steam R$ 50
    SELECT 'Gift Card Steam R$ 50' AS produto,
           'TEST-STM050-A1K7' AS codigo
    UNION ALL
    SELECT 'Gift Card Steam R$ 50', 'TEST-STM050-B2M8'
    UNION ALL
    SELECT 'Gift Card Steam R$ 50', 'TEST-STM050-C3N9'

    UNION ALL

    -- Steam R$ 100
    SELECT 'Gift Card Steam R$ 100', 'TEST-STM100-D4P1'
    UNION ALL
    SELECT 'Gift Card Steam R$ 100', 'TEST-STM100-E5Q2'
    UNION ALL
    SELECT 'Gift Card Steam R$ 100', 'TEST-STM100-F6R3'

    UNION ALL

    -- Xbox R$ 100
    SELECT 'Gift Card Xbox R$ 100', 'TEST-XBX100-G7S4'
    UNION ALL
    SELECT 'Gift Card Xbox R$ 100', 'TEST-XBX100-H8T5'
    UNION ALL
    SELECT 'Gift Card Xbox R$ 100', 'TEST-XBX100-J9V6'

    UNION ALL

    -- Xbox R$ 150
    SELECT 'Gift Card Xbox R$ 150', 'TEST-XBX150-K1W7'
    UNION ALL
    SELECT 'Gift Card Xbox R$ 150', 'TEST-XBX150-L2X8'
    UNION ALL
    SELECT 'Gift Card Xbox R$ 150', 'TEST-XBX150-M3Y9'

    UNION ALL

    -- Playstation R$ 50
    SELECT 'Gift Card Playstation R$ 50', 'TEST-PSN050-N4A1'
    UNION ALL
    SELECT 'Gift Card Playstation R$ 50', 'TEST-PSN050-P5B2'
    UNION ALL
    SELECT 'Gift Card Playstation R$ 50', 'TEST-PSN050-Q6C3'

    UNION ALL

    -- Playstation R$ 100
    SELECT 'Gift Card Playstation R$ 100', 'TEST-PSN100-R7D4'
    UNION ALL
    SELECT 'Gift Card Playstation R$ 100', 'TEST-PSN100-S8E5'
    UNION ALL
    SELECT 'Gift Card Playstation R$ 100', 'TEST-PSN100-T9F6'

    UNION ALL

    -- Nintendo R$ 100
    SELECT 'Gift Card Nintendo Switch R$ 100',
           'TEST-NSW100-U1G7'
    UNION ALL
    SELECT 'Gift Card Nintendo Switch R$ 100',
           'TEST-NSW100-V2H8'
    UNION ALL
    SELECT 'Gift Card Nintendo Switch R$ 100',
           'TEST-NSW100-W3J9'

    UNION ALL

    -- Nintendo R$ 250
    SELECT 'Gift Card Nintendo Switch R$ 250',
           'TEST-NSW250-X4K1'
    UNION ALL
    SELECT 'Gift Card Nintendo Switch R$ 250',
           'TEST-NSW250-Y5L2'
    UNION ALL
    SELECT 'Gift Card Nintendo Switch R$ 250',
           'TEST-NSW250-Z6M3'

) AS dados
INNER JOIN tb_produtos p
    ON p.nome_produto = dados.produto;