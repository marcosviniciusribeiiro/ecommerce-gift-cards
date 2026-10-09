const token = exigirAutenticacao();
const mensagemPedidos = document.getElementById("mensagemPedidos");
const listaPedidos = document.getElementById("listaPedidos");

async function carregarPedidos() {
    try {
        const response = await fetchAutenticado("/api/pedidos/all");

        if (response.status === 401 || response.status === 403) {
            exibirMensagem(
                mensagemPedidos,
                "Você não possui permissão para acessar esta página.",
                "erro"
            );
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

        if(exibirMensagemListaVazia(
            pedidos,
            mensagemPedidos,
            "Nenhum pedido encontrado.")) {
            return;
        }

        pedidos.forEach(function (pedido) {
            const card = document.createElement("article");

            const numeroPedido = document.createElement("h3");
            numeroPedido.textContent = "Pedido #" + pedido.idPedido;
            card.appendChild(numeroPedido);

            const produto = document.createElement("p");

            const produtoStrong = document.createElement("strong");
            produtoStrong.textContent = "Produto: ";

            const produtoSpan = document.createElement("span");
            produtoSpan.textContent = pedido.nomeProduto;

            produto.appendChild(produtoStrong);
            produto.appendChild(produtoSpan);
            card.appendChild(produto);

            const status = document.createElement("p");

            const statusStrong = document.createElement("strong");
            statusStrong.textContent = "Status: "

            const statusSpan = document.createElement("span");
            statusSpan.textContent = pedido.status;

            if (pedido.status === "Pago") {
                statusSpan.classList.add("status-pago");

            } else if (pedido.status === "Pendente") {
                statusSpan.classList.add("status-pendente");

            } else if (pedido.status === "Cancelado") {
                statusSpan.classList.add("status-cancelado");
            }

            status.appendChild(statusStrong);
            status.appendChild(statusSpan);
            card.appendChild(status);

            const data = document.createElement("p");

            const dataStrong = document.createElement("strong");
            dataStrong.textContent = "Data do Pedido: ";

            const dataSpan = document.createElement("span");
            dataSpan.textContent = formatarDataHora(pedido.dataPedido)

            data.appendChild(dataStrong);
            data.appendChild(dataSpan);
            card.appendChild(data);

            const valor = document.createElement("p");

            const valorStrong = document.createElement("strong");
            valorStrong.textContent = "Valor Total: ";

            const valorSpan = document.createElement("span");
            valorSpan.textContent = formatarMoeda(pedido.valorTotal)

            valor.appendChild(valorStrong);
            valor.appendChild(valorSpan);
            card.appendChild(valor);

            if (pedido.status === "Pendente") {
                const confirmarButton = document.createElement("button");
                confirmarButton.textContent = "Confirmar Pagamento";
                confirmarButton.addEventListener("click",() => {
                    confirmarPagamento(
                        pedido.idPedido,
                        statusSpan,
                        confirmarButton,
                        mensagemPedido
                    );
                })
                card.appendChild(confirmarButton);
            }

            const mensagemPedido = document.createElement("p");
            mensagemPedido.classList.add("mensagem");

            card.appendChild(mensagemPedido);

            listaPedidos.appendChild(card);
        });

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

async function confirmarPagamento(
    idPedido,
    elementoStatus,
    botao,
    elementoMensagem
) {
    const confirmar = confirm("Deseja confirmar o pagamento deste pedido?");

    if (!confirmar) {
        return;
    }

    try {
        const response = await fetchAutenticado("/api/pedidos/" + idPedido + "/confirmar", {
            method: "PUT"
        });

        if (tratarErroAutenticacao(response, elementoMensagem)) {
            return;
        }

        if (!response.ok) {
            const erro = await response.json();

            exibirMensagem(
                elementoMensagem,
                erro.mensagem || "Não foi possível confirmar o pagamento.",
                "erro"
            );
            return;
        }

        elementoStatus.textContent = "Pago";
        elementoStatus.classList.remove("status-pendente");
        elementoStatus.classList.add("status-pago");


        botao.remove();

        exibirMensagem(
            elementoMensagem,
            "Pagamento confirmado com sucesso!",
            "sucesso"
        )

    } catch (erro) {
        console.error("Erro ao confirmar pagamento:", erro);

        exibirMensagem(
            mensagemPedido,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
}