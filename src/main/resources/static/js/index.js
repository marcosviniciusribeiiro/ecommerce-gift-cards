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

        produtos.forEach(function (produto){
            const card = document.createElement("article");

            const prod = document.createElement("h3");
            prod.textContent = produto.nome;
            card.appendChild(prod);

            const descricao = document.createElement("p");

            const descricaoStrong = document.createElement("strong");
            descricaoStrong.textContent = "Descrição: ";

            const descricaoSpan = document.createElement("span");
            descricaoSpan.textContent = produto.descricao;

            descricao.appendChild(descricaoStrong);
            descricao.appendChild(descricaoSpan);
            card.appendChild(descricao);

            const plataforma = document.createElement("p");

            const plataformaStrong = document.createElement("strong");
            plataformaStrong.textContent = "Plataforma: ";

            const plataformaSpan = document.createElement("span");
            plataformaSpan.textContent = produto.plataforma;

            plataforma.appendChild(plataformaStrong);
            plataforma.appendChild(plataformaSpan);
            card.appendChild(plataforma);

            const valor = document.createElement("p");

            const valorStrong = document.createElement("strong");
            valorStrong.textContent = "Valor: ";

            const valorSpan = document.createElement("span");
            valorSpan.textContent = Number(produto.valor)
                   .toLocaleString("pt-BR", {
                       style: "currency",
                       currency: "BRL"
                   });

            valor.appendChild(valorStrong);
            valor.appendChild(valorSpan);
            card.appendChild(valor);

            const linkProduto = document.createElement("a");
            linkProduto.textContent = "Ver produto";
            linkProduto.href = "/produto.html?id=" + produto.id;
            card.appendChild(linkProduto);

            listaProdutos.appendChild(card);
        });
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);
        mensagemProdutos.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarProdutos();