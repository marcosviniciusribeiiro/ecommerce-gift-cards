const parametros = new URLSearchParams(window.location.search);
const idPedido = parametros.get("id");
const token = localStorage.getItem("token");

const mensagemPedido = document.getElementById("mensagemPedido");
const codigoGiftCard = document.getElementById("codigoGiftCard");
const codigoButton = document.getElementById("codigoButton");
const cancelarButton = document.getElementById("cancelarButton");
codigoButton.style.display = "none";
cancelarButton.style.display = "none";


if (!token){
    window.location.href = "/login.html"
}

const pedido = document.getElementById("pedido");
const produto = document.getElementById("produto");
const status = document.getElementById("status");
const data = document.getElementById("data");
const valor = document.getElementById("valor");
const codigoSpan = document.getElementById("codigoSpan");

async function carregarPedido() {
    if (!idPedido) {
        mensagemPedido.textContent = "Pedido não informado.";
        return;
    }

    try {
        const response = await fetch("/api/pedidos/me/" + idPedido, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login";
            return;
        }

        if (!response.ok) {
            mensagemPedido.textContent = "Não foi possível encontrar o pedido";
            return;
        }

        const dadosPedido = await response.json();

        pedido.textContent = "Pedido # " + dadosPedido.id;
        produto.textContent = dadosPedido.nomeProduto;

        status.textContent = dadosPedido.statusPedido;
        if (dadosPedido.statusPedido === "Pendente") {
            cancelarButton.style.display = "block";
        } else if (dadosPedido.statusPedido === "Pago") {
            codigoButton.style.display = "block";
        }

        const dataPedido = new Date(dadosPedido.dataPedido);
        data.textContent = dataPedido.toLocaleString("pt-BR", {
            dateStyle: "short",
            timeStyle: "short"
        });

        valor.textContent = Number(dadosPedido.valorTotal)
            .toLocaleString("pt-BR", {
                style: "currency",
                currency: "BRL"
            });

    } catch (erro) {
        console.error("Erro ao carregar o pedido:", erro);
        mensagemPedido.textContent = "Não foi possível conectar ao servidor.";
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
            mensagemPedido.textContent =
                erro.mensagem || "Não foi possível cancelar o pedido.";
            return;
        }

        mensagemPedido.textContent = "Pedido cancelado com sucesso!";

        status.textContent = "Cancelado";
        cancelarButton.style.display = "none";

    } catch (erro) {
        console.error("Erro ao cancelar o pedido:", erro);
        mensagemPedido.textContent = "Não foi possível conectar ao servidor.";
    }
});

codigoButton.addEventListener("click", async function () {
   const confirmar = confirm("Deseja visualizar o código do pedido?")

    if (!confirmar) return;

    try {
        const response = await fetch("/api/pedidos/me/" + idPedido + "/codigos", {
            method: "GET",
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
            mensagemPedido.textContent = "Você não possui acesso a este código.";
            return;
        }

        if (!response.ok) {
            const erro = await response.json();
            mensagemPedido.textContent = erro.mensagem || "Não foi possível obter o código.";
            return;
        }

        const dadosCodigo = await  response.json();

        codigoSpan.style.display = "block";
        codigoGiftCard.textContent = dadosCodigo.codigo;
        codigoButton.style.display = "none";

    } catch (erro) {
        console.error("Erro ao carregar o Gift Card:", erro);
        mensagemPedido.textContent = "Não foi possível conectar ao servidor.";
    }
});