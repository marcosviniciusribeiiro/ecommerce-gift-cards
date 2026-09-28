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
            produto.textContent = "Produto: " + pedido.idProduto;
            card.appendChild(produto);

            const status = document.createElement("p");
            status.textContent = "Status: " + pedido.statusPedido;
            card.appendChild(status);

            const data = document.createElement("p");
            data.textContent = "Data do Pedido: " + pedido.dataPedido;
            card.appendChild(data);

            const valor = document.createElement("p");
            valor.textContent = "Valor Total: " + Number(pedido.valorTotal).toFixed(2);
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