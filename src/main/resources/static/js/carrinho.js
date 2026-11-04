const listaCarrinho = document.getElementById("listaCarrinho");
const valorTotal = document.getElementById("valorTotal");
const finalizarButton = document.getElementById("finalizarButton");
const linkPedidoCriado = document.getElementById("linkPedidoCriado");
const mensagemCarrinho = document.getElementById("mensagemCarrinho");

const token = exigirAutenticacao();

async function carregarCarrinho() {
    if (!token) return;

    finalizarButton.disabled = true;

    const carrinho = obterCarrinho();

    listaCarrinho.replaceChildren();

    if (carrinho.length === 0) {
        valorTotal.textContent = formatarMoeda(0);
        finalizarButton.disabled = true;

        exibirMensagem(
            mensagemCarrinho,
            "Seu carrinho está vazio.",
            "erro"
        );
        return;
    }

    mensagemCarrinho.textContent = "";
    mensagemCarrinho.classList.remove(
        "mensagem-sucesso", "mensagem-erro"
    );

    let total = 0;
    let estoqueInsuficiente = false;

    try {

        for (const item of carrinho) {
            const response = await fetch(`/api/produtos/${item.idProduto}`);

            if (!response.ok) {
                throw new Error(`Não foi possível consultar o produto ${item.idProduto}.`);
            }

            const produto = await response.json();

            const responseEstoque = await fetch(`/api/produtos/${item.idProduto}/estoque`);

            if (!responseEstoque.ok) {
                throw new Error("Não foi possível consultar o estoque.")
            }

            const dadosEstoque = await responseEstoque.json();

            const estoqueDisponivel = dadosEstoque.quantidadeDisponivel;

            if (item.quantidade > estoqueDisponivel) {
                estoqueInsuficiente = true;
            }

            const subtotal = Number(produto.valor) * item.quantidade;
            total += subtotal;

            const card = document.createElement("article");
            card.classList.add("card-carrinho");

            const nome = document.createElement("h3");
            nome.textContent = produto.nome;
            card.appendChild(nome);

            const quantidade = document.createElement("p");
            quantidade.textContent = `Quantidade: ${item.quantidade}`;
            card.appendChild(quantidade);

            const estoque = document.createElement("p");
            estoque.textContent = `Estoque disponível: ${estoqueDisponivel}`;

            if (item.quantidade > estoqueDisponivel) {
                estoque.classList.add("estoque-esgotado");
            } else {
                estoque.classList.add("estoque-disponivel");
            }

            card.appendChild(estoque);

            const valor = document.createElement("p");
            valor.textContent = `Subtotal: ${formatarMoeda(subtotal)}`;
            card.appendChild(valor);

            const controles = document.createElement("div");
            controles.classList.add("controles-carrinho");

            const diminuirButton = document.createElement("button");
            diminuirButton.textContent = "−";
            diminuirButton.type = "button";

            diminuirButton.disabled = item.quantidade <= 1;

            diminuirButton.addEventListener("click", () => {
                alterarQuantidadeCarrinho(item.idProduto, -1);
                carregarCarrinho();
            });

            controles.appendChild(diminuirButton);

            const aumentarButton = document.createElement("button");
            aumentarButton.textContent = "+";
            aumentarButton.type = "button";
            aumentarButton.disabled = item.quantidade >= estoqueDisponivel;

            aumentarButton.addEventListener("click", () => {
               alterarQuantidadeCarrinho(item.idProduto, 1);
               carregarCarrinho();
            });

            controles.appendChild(aumentarButton);

            const removerButton = document.createElement("button");
            removerButton.textContent = "Remover";
            removerButton.type = "button";
            removerButton.classList.add("btn-danger");

            removerButton.addEventListener("click", () => {
               removerDoCarrinho(item.idProduto);
               carregarCarrinho();
            });

            controles.appendChild(removerButton);

            card.appendChild(controles);

            const aviso = document.createElement("p");

            if (item.quantidade > estoqueDisponivel) {
                aviso.textContent = "Quantidade selecionada superior ao estoque disponível.";
                aviso.classList.add("estoque-esgotado");
                card.appendChild(aviso);

            } else if (item.quantidade === estoqueDisponivel) {
                aviso.textContent = "Limite de estoque atingido.";
                aviso.classList.add("estoque-indisponivel");
                card.appendChild(aviso);

            }

            listaCarrinho.appendChild(card);
        }

        valorTotal.textContent = formatarMoeda(total);
        finalizarButton.disabled = estoqueInsuficiente;

    } catch (erro) {
        console.error("Erro ao carregar carrinho:", erro);

        finalizarButton.disabled = true;
        exibirMensagem(
            mensagemCarrinho,
            "Não foi possível carregar os produtos do carrinho.",
            "erro"
        );
    }
}

async function iniciarPaginaCarrinho() {
    if (!token) return;

    finalizarButton.disabled = true;

    try {
        await inicializarCarrinho();

        await carregarCarrinho();

    } catch (erro) {
        console.error("Erro ao inicializar carrinho:", erro);

        exibirMensagem(
            mensagemCarrinho,
            "Não foi possível carregar o carrinho do usuário.",
            "erro"
        );
    }
}

iniciarPaginaCarrinho();

finalizarButton.addEventListener("click", async function () {
   if (!token) return;

   const carrinho = obterCarrinho();

   if (carrinho.length === 0) {
       exibirMensagem(
           mensagemCarrinho,
           "Seu carrinho está vazio.",
           "erro"
       );
       return;
   }

   const confirmar = confirm("Deseja finalizar a compra dos produtos selecionados?");

   if (!confirmar) return;

   finalizarButton.disabled = true;

   try {
       const response = await fetchAutenticado("/api/pedidos/carrinho", {
           method: "POST",
           headers: {
               "Content-Type": "application/json"
           },
           body: JSON.stringify({
               itens: carrinho
           })
       });

       if (tratarErroAutenticacao(response, mensagemCarrinho)) return;

       if (!response.ok) {
           const erro = await response.json()

           exibirMensagem(
               mensagemCarrinho,
               erro.mensagem || "Não foi possível finalizar o pedido.",
               "erro"
           );
           return;
       }

       const pedidoCriado = await response.json();
       limparCarrinho();

       linkPedidoCriado.href = `/pedido.html?id=${pedidoCriado.id}`;

       linkPedidoCriado.style.display = "inline-block";

       exibirMensagem(
           mensagemCarrinho,
           `Pedido #${pedidoCriado.id} criado com sucesso!`,
           "sucesso"
       );

       listaCarrinho.replaceChildren();

       valorTotal.textContent = formatarMoeda(0);

       finalizarButton.disabled = true;

   } catch (erro) {
       console.error("Erro ao finalizar pedido:", erro);

       exibirMensagem(
           mensagemCarrinho,
           "Não foi possível conectar ao servidor.",
           "erro"
       );
   } finally {
       if (obterCarrinho().length > 0) {
           await carregarCarrinho();
       }
   }
});