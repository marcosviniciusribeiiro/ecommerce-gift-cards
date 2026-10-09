const listaProdutos = document.getElementById("listaProdutos");
const mensagemProdutos = document.getElementById("mensagemProdutos");
const token = exigirAutenticacao();

async function carregarProdutos() {
    try {
        const response = await fetch("/api/produtos/all", {
            method: "GET"
        });

        if (!response.ok) {
            exibirMensagem(
                mensagemProdutos,
                "Não foi possível carregar os produtos.",
                "erro"
            );
            return;
        }

        const produtos = await response.json();
        exibirProdutos(produtos);
    } catch (erro) {
        console.error("Erro ao carregar os produtos:", erro);

        exibirMensagem(
            mensagemProdutos,
          "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

carregarProdutos();

function exibirProdutos(produtos) {

    if (exibirMensagemListaVazia(
        produtos,
        mensagemProdutos,
        "Nenhum produto cadastrado.")) {
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
        valorSpan.textContent = formatarMoeda(produto.valor)

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
            () => {
                excluirProduto(
                    produto.id,
                    card,
                    mensagemProduto
                );
            }
        );
        card.appendChild(excluirButton);

        const mensagemProduto = document.createElement("p");
        mensagemProduto.classList.add("mensagem");

        card.appendChild(mensagemProduto);

        listaProdutos.appendChild(card);
    });
}

async function excluirProduto(
    idProduto,
    card,
    mensagemProduto
){
    const confirmar = confirm("Deseja realmente excluir este produto?");

    if (!confirmar) return;

    try {
        const response = await fetchAutenticado("/api/produtos/" + idProduto, {
            method: "DELETE"
        });

        if (tratarErroAutenticacao(response, mensagemProduto)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemProduto,
                erro.mensagem || "Não foi possível excluir o produto.",
                "erro"
            );
            return;
        }

        const mensagemSucesso = document.createElement("p");

        card.after(mensagemSucesso);

        exibirMensagem(
            mensagemSucesso,
            "Produto excluído com sucesso!",
            "sucesso"
        );

        card.remove();

    } catch (erro) {
        console.error("Erro ao excluir o produto:", erro);

        exibirMensagem(
            mensagemProdutos,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}