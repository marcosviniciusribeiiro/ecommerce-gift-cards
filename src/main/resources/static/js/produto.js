const mensagemProduto = document.getElementById("mensagemProduto");

const parametros = new URLSearchParams (window.location.search);

const idProduto = parametros.get("id");

async function carregarProduto() {
    if (!idProduto) {
        mensagemProduto.textContent = "Produto não informado."
        return;
    }

    try {
        const response = await fetch("/api/produtos/" + idProduto);

        if (!response.ok) {
            mensagemProduto.textContent = "Não foi possível encontrar o produto."
            return;
        }

        const produto = await response.json();

        document.getElementById("nomeProduto").textContent = produto.nome;
        document.getElementById("descricaoProduto").textContent = produto.descricao;
        document.getElementById("plataformaProduto").textContent = produto.plataforma;
        document.getElementById("valorProduto").textContent = "R$ " + Number(produto.valor).toFixed(2);
    } catch (erro) {
        console.error("Erro ao carregar o produto:", erro);

        mensagemProduto.textContent = "Não foi possível conectar ao servidor."

    }
}

carregarProduto();