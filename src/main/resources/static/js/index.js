const mensagemProdutos = document.getElementById("mensagemProdutos");
const listaProdutos = document.getElementById("listaProdutos");

async function carregarProdutos(){
    try {
        const response = await fetch("/api/produtos/all", {
            method: "GET",
        });

        if (!response.ok) {
            mensagemProdutos.textContent = "Não foi possível carregar os produtos.";
            return;
        }

        const produtos = await response.json();

        console.log(produtos);

        produtos.forEach(function (produto){
           const card = document.createElement("article");

           const nome = document.createElement("h3");
           nome.textContent = produto.nome;
           card.appendChild(nome);

           const descricao = document.createElement("p");
           descricao.textContent = produto.descricao;
           card.appendChild(descricao);

           const plataforma = document.createElement("p");
           plataforma.textContent = "Plataforma: " + produto.plataforma;
           card.appendChild(plataforma);

           const valor = document.createElement("p");
           valor.textContent = "R$ " + Number(produto.valor).toFixed(2);
           card.appendChild(valor);

           listaProdutos.appendChild(card);
        });
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);
        mensagemProdutos.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarProdutos();