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

    if (!token) return;

    if (!idPedido) {
        exibirMensagem(
            mensagemPedido,
            "Pedido não informado.",
            "erro"
        );
        return;
    }

    try {
        const response = await fetchAutenticado(`/api/pedidos/me/${idPedido}`);

        if (tratarErroAutenticacao(response, mensagemPedido)) {
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

        produto.replaceChildren();

        const listaProdutos = document.createElement("ul");

        dadosPedido.itens.forEach(function (item) {
            const elementoItem = document.createElement("li");

            elementoItem.textContent = `${item.nomeProduto} — ${formatarMoeda(item.valorUnitario)}`;

            listaProdutos.appendChild(elementoItem);
        });

        produto.appendChild(listaProdutos);

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
        const response = await fetchAutenticado(`/api/pedidos/me/${idPedido}/cancelar`, {
            method: "PUT",
        });

        if (tratarErroAutenticacao(response, mensagemPedido)) {
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

        status.classList.remove("status-pendente");
        status.classList.add("status-cancelado");

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
        const response = await fetchAutenticado(`/api/pedidos/me/${idPedido}/codigos`);

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

        const dadosCodigo = await response.json();

        codigoGiftCard.replaceChildren();

        const listaCodigos = document.createElement("ul");

        dadosCodigo.itens.forEach(function (item) {
            const elementoItem = document.createElement("li");

            const nomeProduto = document.createElement("strong");
            nomeProduto.textContent = `${item.nomeProduto}: `;
            elementoItem.appendChild(nomeProduto);

            const valorCodigo = document.createElement("span");
            valorCodigo.textContent = item.codigo;
            elementoItem.appendChild(valorCodigo);

            listaCodigos.appendChild(elementoItem);
        });

        codigoGiftCard.appendChild(listaCodigos);

        codigoSpan.style.display = "block";
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