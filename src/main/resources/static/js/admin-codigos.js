const parametros = new URLSearchParams(window.location.search);
const idProduto = parametros.get("id");

const spanProduto = document.getElementById("produto");

const listaCodigos = document.getElementById("listaCodigos");
const mensagemCodigos = document.getElementById("mensagemCodigos");

const token = exigirAutenticacao();

const linkCadastrarCodigo = document.getElementById("linkCadastrarCodigo");

if (idProduto) {
    linkCadastrarCodigo.href = `cadastrar-codigo.html?id=${idProduto}`;
} else {
    linkCadastrarCodigo.remove();
}

async function carregarCodigos() {
    if (!token) return;

    if (!idProduto) {
        exibirMensagem(
            mensagemCodigos,
            "Produto não informado",
            "erro"
        );
        return;
    }

    const produto = await buscarProduto(idProduto);
    spanProduto.textContent = produto.nome;

    try {
        const response = await fetchAutenticado(`/api/codigos/produto/${idProduto}`);

        if (tratarErroAutenticacao(response, mensagemCodigos)) return;

        if (!response.ok) {
            exibirMensagem(
                mensagemCodigos,
                "Não foi possível carregar os códigos.",
                "erro"
            );
            return;
        }

        const codigos = await response.json();

        if (exibirMensagemListaVazia(
            codigos,
            mensagemCodigos,
            "Nenhum código cadastrado para este produto."
        )) {
            return;
        }

        codigos.forEach(function (codigo) {
            const card = document.createElement("article");

            const titulo = document.createElement("h3");
            titulo.textContent = `Código #${codigo.id}`;
            card.appendChild(titulo);

            const valor = document.createElement("p");
            valor.textContent = "Código: ••••••••";
            card.appendChild(valor);

            const status = document.createElement("p");
            status.textContent = `Status: ${codigo.status}`;

            if (codigo.status === "Disponivel") {
                status.classList.add("estoque-disponivel");
            } else {
                status.classList.add("estoque-esgotado");
            }

            card.appendChild(status);

            if (codigo.status === "Disponivel") {
                const linkEditar = document.createElement("a");

                linkEditar.textContent = "Editar";

                linkEditar.href =
                    `/atualizar-codigo.html?id=${codigo.id}&idProduto=${idProduto}`;

                linkEditar.classList.add("link-button");

                card.appendChild(linkEditar);
            }

            listaCodigos.appendChild(card);
        });

    } catch (erro) {
        console.error("Erro ao carregar códigos:", erro);

        exibirMensagem(
            mensagemCodigos,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

carregarCodigos();

async function buscarProduto(idProduto) {
    let dadosProduto;

    try {
        const response = await fetch(`/api/produtos/${idProduto}`);

        return dadosProduto = await response.json();
    } catch (erro) {
        console.error("Erro ao buscar produto:", erro);

        exibirMensagem(
            mensagemCodigos,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}