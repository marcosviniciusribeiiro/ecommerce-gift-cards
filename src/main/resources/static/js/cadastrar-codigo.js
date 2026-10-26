const formCodigo = document.getElementById("formCodigo")
const mensagemCodigo = document.getElementById("mensagemCodigo");
const token = exigirAutenticacao();

const parametros = new URLSearchParams(window.location.search);
const idProduto = parametros.get("id");

formCodigo.addEventListener("submit", async function (event) {
    event.preventDefault();

    if (!token) return;

    if (!idProduto) {
        exibirMensagem(
            mensagemCodigo,
            "Produto não informado.",
            "erro"
        );
        return;
    }

    const codigo = document.getElementById("codigo").value.trim();

    if (!codigo) {
        exibirMensagem(
            mensagemCodigo,
            "Informe um código válido.",
            "erro"
        );
        return;
    }

    try {
        const response = await fetchAutenticado("/api/codigos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                idProduto: Number(idProduto),
                codigo: codigo
            })
        });

        if (tratarErroAutenticacao(response, mensagemCodigo)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemCodigo,
                erro.mensagem || "Não foi possível cadastrar o código.",
                "erro"
            );
            return;
        }

        exibirMensagem(
            mensagemCodigo,
            "Código cadastrado com sucesso!",
            "sucesso"
        );

        formCodigo.reset();
    } catch (erro) {
        console.error("Erro ao cadastrar código:", erro);

        exibirMensagem(
            mensagemCodigo,
            "Não foi possível conectar ao servidor.",
            "erro"
        )
    }
})