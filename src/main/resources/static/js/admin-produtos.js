const listaProdutos = document.getElementById("listaProdutos");
const mensagemProdutos = document.getElementById("mensagemProdutos");
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/login.html";
}

async function carregarProdutos() {
    try {
        const response = await fetch("/api/produtos/all", {
            method: "GET"
        });

        if (!response.ok) {
            mensagemProdutos.textContent = "Não foi possível carregar os produtos.";
            return;
        }

        const produtos = await response.json();
        exibirProdutos(produtos);
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);
        mensagemProdutos.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarProdutos();

function exibirProdutos(produtos) {

    if (produtos.length === 0) {
        mensagemProdutos.textContent = "Nenhum produto cadastrado.";
        return;
    }

    produtos.forEach(function (produto) {
        const card = document.createElement("article");

        const nome = document.createElement("h3");
        nome.textContent = produto.nome;
        card.appendChild(nome);

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


        const linkEditar = document.createElement("a");
        linkEditar.textContent = "Editar";
        linkEditar.href = "/atualizar-produto.html?id=" + produto.id;
        linkEditar.classList.add("link-button");


        card.appendChild(linkEditar);

        const excluirButton = document.createElement("button");
        excluirButton.textContent = "Excluir";
        excluirButton.classList.add("btn-danger");
        excluirButton.addEventListener(
            "click",
            function () {
                excluirProduto(
                    produto.id,
                    card
                );
            }
        );

        card.appendChild(excluirButton);

        listaProdutos.appendChild(card);
    });
}

async function excluirProduto(
    idProduto,
    card
){
    const confirmar = confirm("Deseja realmente excluir este produto?");

    if (!confirmar) return;

    try {
        const response = await fetch("/api/produtos/" + idProduto, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        if (response.status === 403) {
            mensagemProdutos.textContent = "Você não possui permissão para excluir produtos.";
            return;
        }

        if (!response.ok) {
            const erro = await response.json();
            mensagemProdutos.textContent = erro.mensagem || "Não foi possível excluir o produto.";
            return;
        }

        card.remove();

        mensagemProdutos.textContent = "Produto excluído com sucesso!";

    } catch (erro) {
        console.error("Erro ao excluir o produto:", erro);
        mensagemProdutos.textContent = "Não foi posível conectar ao servidor.";
    }
}