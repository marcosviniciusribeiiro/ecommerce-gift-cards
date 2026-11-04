const listaPedidos = document.getElementById("listaPedidos");
const mensagemPedidos = document.getElementById("mensagemPedidos");
const token = exigirAutenticacao();

async function carregarPedidos() {

    if (!token) return;

    try {
        const response = await fetchAutenticado("/api/pedidos/me");

        if (tratarErroAutenticacao(response, mensagemPedidos)) {
            return;
        }

        if (!response.ok) {
            exibirMensagem(
                mensagemPedidos,
                "Não foi possível carregar os pedidos.",
                "erro"
            );
            return;
        }

        const pedidos = await response.json();

        listaPedidos.replaceChildren();

        if (exibirMensagemListaVazia(
            pedidos,
            mensagemPedidos,
            "Você ainda não possui pedidos.")) {
            return;
        }

        pedidos.forEach(function (pedido) {
            const card = document.createElement("article");

            const numero = document.createElement("h3");
            numero.textContent = "Pedido #" + pedido.id;
            card.appendChild(numero);

            const produtos = document.createElement("div");

            const produtosStrong = document.createElement("strong");
            produtosStrong.textContent = "Produto(s):";

            produtos.appendChild(produtosStrong)

            const listaItens = document.createElement("ul");

            pedido.itens.forEach(function (item) {
                const elementoItem = document.createElement("li");

                elementoItem.textContent = `${item.nomeProduto} — ${formatarMoeda(item.valorUnitario)}`;

                listaItens.appendChild(elementoItem);
            });

            produtos.appendChild(listaItens);
            card.appendChild(produtos);

            const status = document.createElement("p");

            const statusStrong = document.createElement("strong");
            statusStrong.textContent = "Status: ";

            const statusSpan = document.createElement("span");
            statusSpan.textContent = pedido.statusPedido;

            if (pedido.statusPedido === "Pago") {
                statusSpan.classList.add("status-pago");

            } else if (pedido.statusPedido === "Pendente") {
                statusSpan.classList.add("status-pendente");

            } else if (pedido.statusPedido === "Cancelado") {
                statusSpan.classList.add("status-cancelado");
            }

            status.appendChild(statusStrong);
            status.appendChild(statusSpan);
            card.appendChild(status);

            const data = document.createElement("p");

            const dataStrong = document.createElement("strong");
            dataStrong.textContent = "Data do Pedido: ";

            const dataSpan = document.createElement("span");
            dataSpan.textContent = formatarDataHora(pedido.dataPedido);

            data.appendChild(dataStrong);
            data.appendChild(dataSpan);
            card.appendChild(data);

            const valor = document.createElement("p");

            const valorStrong = document.createElement("strong");
            valorStrong.textContent = "Valor Total: ";

            const valorSpan = document.createElement("span");
            valorSpan.textContent = formatarMoeda(pedido.valorTotal);

            valor.appendChild(valorStrong);
            valor.appendChild(valorSpan);
            card.appendChild(valor);

            const linkPedido = document.createElement("a");
            linkPedido.textContent = "Ver Pedido";
            linkPedido.href = "/pedido.html?id=" + pedido.id;
            card.appendChild(linkPedido);

            listaPedidos.appendChild(card);
        })

    } catch (erro) {
        console.error("Erro ao carregar os pedidos:", erro);

        exibirMensagem(
            mensagemPedidos,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}

carregarPedidos();