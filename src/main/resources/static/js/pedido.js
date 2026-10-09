const parametros = new URLSearchParams(window.location.search);
const idPedido = parametros.get("id");
const token = exigirAutenticacao();

const mensagemPedido = document.getElementById("mensagemPedido");
const codigoGiftCard = document.getElementById("codigoGiftCard");
const codigoButton = document.getElementById("codigoButton");
const cancelarButton = document.getElementById("cancelarButton");
codigoButton.style.display = "none";
cancelarButton.style.display = "none";

const pedido = document.getElementById("pedido");
const produto = document.getElementById("produto");
const status = document.getElementById("status");
const data = document.getElementById("data");
const valor = document.getElementById("valor");
const codigoSpan = document.getElementById("codigoSpan");

async function carregarPedido() {
    if (!idPedido) {
        exibirMensagem(
            mensagemPedido,
            "Pedido não informado.",
            "erro"
        );
        return;
    }

    try {
        const response = await fetchAutenticado("/api/pedidos/me/" + idPedido);

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login";
            return;
        }

        if (!response.ok) {
            exibirMensagem(
                mensagemPedido,
                "Não foi possível encontrar o pedido",
                "erro"
            );
            return;
        }

        const dadosPedido = await response.json();

        pedido.textContent = "Pedido # " + dadosPedido.id;
        produto.textContent = dadosPedido.nomeProduto;

        const statusPedido = dadosPedido.statusPedido;

        status.textContent = statusPedido;

        if (statusPedido === "Pago") {
            status.classList.add("status-pago");
            codigoButton.style.display = "block";
        }
        else if (statusPedido === "Pendente") {
            status.classList.add("status-pendente");
            cancelarButton.classList.add("btn-danger");
            cancelarButton.style.display = "block";
        } else if (statusPedido === "Cancelado") {
            status.classList.add("status-cancelado");
        }

        data.textContent = formatarDataHora(dadosPedido.dataPedido);

        valor.textContent = formatarMoeda(dadosPedido.valorTotal);
    } catch (erro) {
        console.error("Erro ao carregar o pedido:", erro);

        exibirMensagem(
            mensagemPedido,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

carregarPedido();

cancelarButton.addEventListener("click", async function () {
    const confirmar = confirm("Deseja realmente cancelar este pedido?");

    if (!confirmar) return;

    try {
        const response = await fetch("/api/pedidos/me/" + idPedido + "/cancelar", {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemPedido,
                erro.mensagem || "Não foi possível cancelar o pedido.",
                "erro"
            );
            return;
        }

        exibirMensagem(
            mensagemPedido,
            "Pedido cancelado com sucesso!",
            "sucesso"
        );

        status.textContent = "Cancelado";
        cancelarButton.style.display = "none";

    } catch (erro) {
        console.error("Erro ao cancelar o pedido:", erro);

        exibirMensagem(
            mensagemPedido,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});

codigoButton.addEventListener("click", async function () {
   const confirmar = confirm("Deseja visualizar o código do pedido?")

    if (!confirmar) return;

    try {
        const response = await fetchAutenticado("/api/pedidos/me/" + idPedido + "/codigos");

        if (tratarErroAutenticacao(response, mensagemPedido)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                mensagemPedido,
                erro.mensagem || "Não foi possível obter o código.",
                "erro"
            );
            return;
        }

        const dadosCodigo = await  response.json();

        codigoSpan.style.display = "block";
        codigoGiftCard.textContent = dadosCodigo.codigo;
        codigoButton.style.display = "none";

    } catch (erro) {
        console.error("Erro ao carregar o Gift Card:", erro);

        exibirMensagem(
            mensagemPedido,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});