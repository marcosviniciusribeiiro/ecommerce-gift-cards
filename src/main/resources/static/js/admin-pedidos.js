const token = localStorage.getItem("token");
const mensagemPedidos = document.getElementById("mensagemPedidos");
const listaPedidos = document.getElementById("listaPedidos");

if (!token) {
    window.location.href = "/login.html";
}

async function carregarPedidos() {
    try {
        const response = await fetch("/api/pedidos/all", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401 || response.status === 403) {
            mensagemPedidos.textContent = "Você não possui permissão para acessar esta página.";
            return;
        }

        if (!response.ok) {
            mensagemPedidos.textContent = "Não foi possível carregar os pedidos.";
            return;
        }

        const pedidos = await response.json();

        if (pedidos.length === 0) {
            mensagemPedidos.textContent = "Nenhum pedido encontrado.";
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

            const dataPedido = new Date(pedido.dataPedido);
            dataSpan.textContent = dataPedido.toLocaleString("pt-BR", {
                dateStyle: "short",
                timeStyle: "short"
            });

            data.appendChild(dataStrong);
            data.appendChild(dataSpan);
            card.appendChild(data);

            const valor = document.createElement("p");

            const valorStrong = document.createElement("strong");
            valorStrong.textContent = "Valor Total: ";

            const valorSpan = document.createElement("span");
            valorSpan.textContent = Number(pedido.valorTotal)
                    .toLocaleString("pt-BR", {
                        style: "currency",
                        currency: "BRL"
                    });

            valor.appendChild(valorStrong);
            valor.appendChild(valorSpan);
            card.appendChild(valor);

            if (pedido.status === "Pendente") {
                const confirmarButton = document.createElement("button");
                confirmarButton.textContent = "Confirmar Pagamento";
                confirmarButton.addEventListener("click",function () {
                    confirmarPagamento(
                        pedido.idPedido,
                        statusSpan,
                        confirmarButton
                    );
                })
                card.appendChild(confirmarButton);
            }

            listaPedidos.appendChild(card);
        });

    } catch (erro) {
        console.error("Erro ao carregar os pedidos:", erro);
        mensagemPedidos.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarPedidos();

async function confirmarPagamento(
    idPedido,
    elementoStatus,
    botao
) {
    const confirmar = confirm("Deseja confirmar o pagamento deste pedido?");

    if (!confirmar) {
        return;
    }

    try {
        const response = await fetch(
            "/api/pedidos/" + idPedido + "/confirmar", {
                method: "PUT",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href =
                "/login.html";
            return;
        }

        if (response.status === 403) {
            mensagemPedidos.textContent =
                "Você não possui permissão para confirmar pedidos.";
            return;
        }

        if (!response.ok) {
            const erro = await response.json();
            mensagemPedidos.textContent = erro.mensagem || "Não foi possível confirmar o pagamento.";
            return;
        }

        elementoStatus.textContent = "Pago";
        elementoStatus.classList.remove("status-pendente");
        elementoStatus.classList.add("status-pago");


        botao.remove();

        mensagemPedidos.textContent = "Pagamento confirmado com sucesso!";

    } catch (erro) {
        console.error("Erro ao confirmar pagamento:", erro);
        mensagemPedidos.textContent = "Não foi possível conectar ao servidor.";
    }
}