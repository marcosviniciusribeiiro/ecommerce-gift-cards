USE ecommerce_db;

/* Consultas do Ecommerce */

/* 1. USUÁRIOS */

-- Consultar usuários cadastrados
SELECT
    id,
    nome_usuario,
    email_usuario,
    tipo_usuario,
    data_cadastro
FROM tb_usuarios;

-- Consultar administradores
SELECT
    id,
    nome_usuario,
    email_usuario
FROM tb_usuarios
WHERE tipo_usuario = 'Administrador';

/* 2. PRODUTOS */

-- Consultar todos os produtos
SELECT *
FROM tb_produtos
ORDER BY plataforma, nome_produto;

-- Quantidade de produtos por plataforma
SELECT
    plataforma,
    COUNT(*) AS total_produtos
FROM tb_produtos
GROUP BY plataforma;

/* 3. ESTOQUE DE GIFT CARDS */

-- Consultar quantidade de códigos disponíveis por produto
SELECT
    p.id,
    p.nome_produto,
    p.plataforma,
    COUNT(c.id) AS estoque_disponivel
FROM tb_produtos p
LEFT JOIN tb_codigos_giftcard c
    ON c.id_produto = p.id
    AND c.status = 'Disponivel'
GROUP BY p.id, p.nome_produto, p.plataforma
ORDER BY p.plataforma, p.nome_produto;

-- Resumo geral dos códigos
SELECT
    status,
    COUNT(*) AS quantidade
FROM tb_codigos_giftcard
GROUP BY status;

/* 4. PEDIDOS */

-- Consultar pedidos e seus respectivos clientes
SELECT
    p.id AS id_pedido,
    u.nome_usuario AS cliente,
    p.status,
    p.data_pedido,
    p.valor_total
FROM tb_pedidos p
INNER JOIN tb_usuarios u
    ON u.id = p.id_usuario
ORDER BY p.data_pedido DESC;

-- Consultar itens dos pedidos
SELECT
    i.id AS id_item,
    i.id_pedido,
    p.nome_produto,
    i.valor_unitario
FROM tb_itens_pedido i
INNER JOIN tb_produtos p
    ON p.id = i.id_produto
ORDER BY i.id_pedido DESC, i.id;

/* 5. PAGAMENTOS */

-- Consultar pagamentos associados aos pedidos
SELECT
    pg.id_pagamento,
    pg.id_pedido,
    pg.id_pagamento_asaas,
    pg.forma_pagamento,
    pg.valor,
    pg.status,
    pg.data_criacao,
    pg.data_confirmacao
FROM tb_pagamentos pg
ORDER BY pg.data_criacao DESC;

/* 6. WEBHOOKS */

-- Consultar eventos recebidos do Asaas
SELECT
    id_evento_asaas,
    tipo_evento,
    id_pagamento_asaas,
    status,
    data_recebimento,
    data_processamento
FROM tb_eventos_webhook
ORDER BY data_recebimento DESC;

-- Tempo de processamento dos eventos
SELECT
    id_evento_asaas,
    tipo_evento,
    status,
    TIMESTAMPDIFF(
        SECOND,
        data_recebimento,
        data_processamento
    ) AS tempo_processamento_segundos
FROM tb_eventos_webhook
WHERE data_processamento IS NOT NULL
ORDER BY data_recebimento DESC;

/* 7. RELATÓRIOS DE VENDAS */

-- Resumo geral das vendas
SELECT
    COUNT(*) AS total_pedidos_pagos,
    COALESCE(SUM(valor_total), 0) AS faturamento_total
FROM tb_pedidos
WHERE status = 'Pago';

-- Quantidade total de gift cards vendidos
SELECT
    COUNT(i.id) AS total_giftcards_vendidos
FROM tb_itens_pedido i
INNER JOIN tb_pedidos p
    ON p.id = i.id_pedido
WHERE p.status = 'Pago';

-- Ranking de produtos mais vendidos
SELECT
    pr.id AS id_produto,
    pr.nome_produto,
    COUNT(i.id) AS quantidade_vendida,
    SUM(i.valor_unitario) AS faturamento
FROM tb_itens_pedido i
INNER JOIN tb_produtos pr
    ON pr.id = i.id_produto
INNER JOIN tb_pedidos ped
    ON ped.id = i.id_pedido
WHERE ped.status = 'Pago'
GROUP BY pr.id, pr.nome_produto
ORDER BY quantidade_vendida DESC, pr.nome_produto ASC;

/* 8. RECUPERAÇÃO DE SENHA */

-- Quantidade de tokens por situação
SELECT
    utilizado,
    COUNT(*) AS quantidade
FROM tb_tokens_recuperacao
GROUP BY utilizado;