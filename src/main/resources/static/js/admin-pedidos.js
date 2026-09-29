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
            mensagemPedidos.textContent = "Voçê não possui permissão para acessar esta página.";
            return;
        }

        if (!response.ok) {
            mensagemPedidos.textContent = "Não foi possível carregar os pedidos.";
            return;
        }

        const pedidos = await response.json();

        if (pedidos === 0) {
            mensagemPedidos.textContent = "Nenhum pedido encontrado.";
            return;
        }

        pedidos.forEach(function (pedido) {
            const card = document.createElement("article");

            const numeroPedido = document.createElement("h3");
            numeroPedido.textContent = "Pedido #" + pedido.idPedido;
            card.appendChild(numeroPedido);

            const produto = document.createElement("p");
            produto.textContent = "Produto: " + pedido.idProduto;
            card.appendChild(produto);

            const status = document.createElement("p");
            status.textContent = "Status: " + pedido.status;
            card.appendChild(status);

            const data = document.createElement("p");
            data.textContent = "Data do Pedido: " + pedido.dataPedido;
            card.appendChild(data);

            const valor = document.createElement("p");
            valor.textContent = "Valor Total: " + Number(pedido.valorTotal).toFixed(2);
            card.appendChild(valor);

            if (pedido.status === "Pendente") {
                const confirmarButton = document.createElement("button");
                confirmarButton.textContent = "Confirmar Pagamento";
                confirmarButton.addEventListener("click",function () {
                    confirmarPagamento(
                        pedido.idPedido,
                        status,
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

        elementoStatus.textContent = "Status: Pago";

        botao.remove();

        mensagemPedidos.textContent = "Pagamento confirmado com sucesso!";

    } catch (erro) {
        console.error("Erro ao confirmar pagamento:", erro);

        mensagemPedidos.textContent = "Não foi possível conectar ao servidor.";
    }
}