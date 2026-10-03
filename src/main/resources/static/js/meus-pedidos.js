const listaPedidos = document.getElementById("listaPedidos");
const mensagemPedidos = document.getElementById("mensagemPedidos");
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/login.html";
}

async function carregarPedidos() {
    try {
        const response = await fetch("/api/pedidos/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html"
            return;
        }

        if (!response.ok) {
            mensagemPedidos.textContent = "Não foi possível carregar os pedidos.";
            return;
        }

        const pedidos = await response.json();

        if (pedidos.length === 0) {
            mensagemPedidos.textContent = "Você ainda não possui pedidos.";
            return;
        }

        pedidos.forEach(function (pedido) {
            const card = document.createElement("article");

            const numero = document.createElement("h3");
            numero.textContent = "Pedido #" + pedido.id;
            card.appendChild(numero);

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

            const linkPedido = document.createElement("a");
            linkPedido.textContent = "Ver Pedido";
            linkPedido.href = "/pedido.html?id=" + pedido.id;
            card.appendChild(linkPedido);

            listaPedidos.appendChild(card);
        })

    } catch (erro) {
        console.error("Erro ao carregar os pedidos:", erro);
        mensagemPedidos.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarPedidos();