const formulario = document.getElementById("atualizarProdutoForm");
const token = localStorage.getItem("token");
const mensagemAtualizar = document.getElementById("mensagemAtualizar");
const parametros = new URLSearchParams(window.location.search);
const idProduto = parametros.get("id");

if (!token) {
    window.location.href = "/login.html";
}

const nome = document.getElementById("nome");
const descricao = document.getElementById("descricao");
const plataforma = document.getElementById("plataforma");
const valor = document.getElementById("valor");

async function carregarProduto() {
    if (!idProduto) {
        exibirMensagem(
            mensagemAtualizar,
            "Produto não informado.",
            "erro"
        );
        return;
    }

    try {
        const response = await fetch(
            "/api/produtos/" + idProduto,
            {
                method: "GET"
            }
        );

        if (!response.ok) {
            exibirMensagem(
                mensagemAtualizar,
                "Não foi possível encontrar o produto.",
                "erro"
            );
            return;
        }

        const produto = await response.json();

        nome.value = produto.nome;
        descricao.value = produto.descricao;
        plataforma.value = produto.plataforma;
        valor.value = produto.valor;

    } catch (erro) {
        console.error("Erro ao carregar produto:", erro);

        exibirMensagem(
            mensagemAtualizar,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

carregarProduto();

formulario.addEventListener("submit", async function (event){
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const descricao = document.getElementById("descricao").value;
    const plataforma = document.getElementById("plataforma").value;
    const valor = document.getElementById("valor").value;

    try {
        const response = await fetch("/api/produtos/" + idProduto, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nome: nome,
                descricao: descricao,
                plataforma: plataforma,
                valor: Number(valor)
            })
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        if (response.status === 403) {
            exibirMensagem(
                mensagemAtualizar,
                "Você não possui permissão para atualizar produtos.",
                "erro"
            );
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemAtualizar,
                erro.mensagem || "Não foi possível atualizar o produto.",
                "erro"
            );
            return;
        }

        exibirMensagem(
            mensagemAtualizar,
            "Produto atualizado com sucesso!",
            "sucesso"
        );

        setTimeout(function () {
            window.location.href = "/admin-produtos.html";
        }, 1500);
    } catch (erro) {
        console.error("Erro ao atualizar o produto:", erro);

        exibirMensagem(
            mensagemAtualizar,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});