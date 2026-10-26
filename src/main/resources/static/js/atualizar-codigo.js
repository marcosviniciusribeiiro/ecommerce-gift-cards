const parametros = new URLSearchParams(window.location.search);
const idCodigo = parametros.get("id");
const idProduto = parametros.get("idProduto");

alert("ok");

const formAtualizarCodigo = document.getElementById("formAtualizarCodigo");
const campoCodigo = document.getElementById("codigo");
const mensagemCodigo = document.getElementById("mensagemCodigo");;
const voltarCodigos = document.getElementById("voltarCodigos");

const token = exigirAutenticacao();

if (idProduto) {
    voltarCodigos.href = `/admin-codigos.html?id=${idProduto}`;
}

async  function carregarCodigo() {
    if (!token) return;

    if (!idCodigo) {
        exibirMensagem(
            mensagemCodigo,
            "Código não informado",
            "erro"
        );
        formAtualizarCodigo.style.display = "none";
        return;
    }

    try {
        const response = await fetchAutenticado(`/api/codigos/${idCodigo}`);

        if (tratarErroAutenticacao(response, mensagemCodigo)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();
            mensagemCodigo.textContent = erro.mensagem || "Não foi possível carregar o código.";
        }

        const codigo = await response.json();

        if (codigo.status !== "Disponivel") {
            exibirMensagem(
                mensagemCodigo,
                "Não é possível editar um código vendido.",
                "erro"
            );

            formAtualizarCodigo.style.display = "none";
            return;
        }

        campoCodigo.value = codigo.codigo;

    } catch (erro) {
        console.error("Erro ao atualizar código:", erro);

        exibirMensagem(
            mensagemCodigo,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

formAtualizarCodigo.addEventListener("submit", async function (event) {
    event.preventDefault();

    if (!token || !idCodigo) return;

    const novoCodigo = campoCodigo.value.trim();

    if (!novoCodigo) {
        exibirMensagem(
            mensagemCodigo,
            "Informe um código válido.",
            "erro"
        );
        return;
    }

    try {
        const response = fetchAutenticado(`/api/codigos/${idCodigo}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                codigo: novoCodigo
            })
        });

        if (tratarErroAutenticacao(response, mensagemCodigo)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemCodigo,
                erro.mensagem || "Não foi possível atualizar o código.",
                "erro"
            );
            return;
        }

        exibirMensagem(
            mensagemCodigo,
            "Código atualizado com sucesso!",
            "sucesso"
        );
    } catch (erro) {
        console.error("Erro ao atualizar código:", erro);

        exibirMensagem(
            mensagemCodigo,
            "Não foi possível atualizar o código.",
            "erro"
        );
    }
});

carregarCodigo();