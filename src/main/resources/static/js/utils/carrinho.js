let CHAVE_CARRINHO = null;

async function inicializarCarrinho() {
    const response = await fetchAutenticado("/api/usuarios/me");

    if (!response.ok) {
        throw new Error("Não foi possível identificar o usuário.");
    }

    const usuario = await response.json();

    CHAVE_CARRINHO = `carrinho_${usuario.id}`;
}

function obterChaveCarrinho() {
    if (!CHAVE_CARRINHO) {
        throw new Error("Carrinho não inicializado.");
    }

    return CHAVE_CARRINHO;
}

function obterCarrinho() {
    return JSON.parse(
        localStorage.getItem(obterChaveCarrinho()) || "[]"
    );
}

function adicionarAoCarrinho(idProduto) {
    const carrinho = JSON.parse(
        localStorage.getItem(obterChaveCarrinho()) || "[]"
    );

    const produtoExistente = carrinho.find(
        item => item.idProduto === Number(idProduto)
    );

    if (produtoExistente) {
        produtoExistente.quantidade += 1;
    } else {
        carrinho.push({
            idProduto: Number(idProduto),
            quantidade: 1
        });
    }

    localStorage.setItem(
        obterChaveCarrinho(),
        JSON.stringify(carrinho)
    );
}

function  alterarQuantidadeCarrinho(idProduto, variacao) {
    const carrinho = obterCarrinho();

    const item = carrinho.find(
        item => item.idProduto === Number(idProduto)
    );

    if (!item) return;

    const novaQuantidade = item.quantidade + variacao;

    if (novaQuantidade < 1) return;

    item.quantidade = novaQuantidade;

    localStorage.setItem(
        obterChaveCarrinho(),
        JSON.stringify(carrinho)
    );
}

function removerDoCarrinho(idProduto) {
    const carrinho = obterCarrinho();

    const carrinhoAtualizado = carrinho.filter(
        item => item.idProduto !== Number(idProduto)
    );

    localStorage.setItem(
        obterChaveCarrinho(),
        JSON.stringify(carrinhoAtualizado)
    );
}

function limparCarrinho() {
    localStorage.removeItem(obterChaveCarrinho());
}