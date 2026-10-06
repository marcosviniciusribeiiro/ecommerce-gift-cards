const mensagemProduto = document.getElementById("mensagemProduto");
const parametros = new URLSearchParams (window.location.search);
const idProduto = parametros.get("id");
const comprarButton = document.getElementById("comprarButton");

const nome = document.getElementById("nomeProduto");
const descricao = document.getElementById("descricaoProduto");
const plataforma = document.getElementById("plataformaProduto");
const valor = document.getElementById("valorProduto");

async function carregarProduto() {
    if (!idProduto) {
        exibirMensagem(
            mensagemProduto,
            "Produto não informado.",
            "erro"
        );
        return;
    }

    try {
        const response = await fetch("/api/produtos/" + idProduto);

        if (!response.ok) {
            exibirMensagem(
                mensagemProduto,
                "Não foi possível encontrar o produto.",
                "erro"
            );
            return;
        }

        const produto = await response.json();

        nome.textContent = produto.nome;

        descricao.textContent = produto.descricao;

        plataforma.textContent = produto.plataforma;

        valor.textContent = formatarMoeda(produto.valor)

    } catch (erro) {
        console.error("Erro ao carregar o produto:", erro);

        exibirMensagem(
            mensagemProduto,
            "Não foi possível conectar ao servidor.",
            "erro"
        )
    }
}

carregarProduto();

comprarButton.addEventListener("click", async function (){
   const token = localStorage.getItem("token");

   if (!token){
       window.location.href = "/login.html";
       return;
   }

   try {
       const response = await fetch("/api/pedidos", {
           method: "POST",
           headers: {
               "Content-Type": "application/json",
               "Authorization": "Bearer " + token
           },
           body: JSON.stringify({
               idProduto: Number(idProduto)
           })
       });

       if (response.status === 401 || response.status === 403) {
           localStorage.removeItem("token");
           window.location.href = "/login.html";
           return;
       }

       const data = await response.json();

       if (!response.ok) {
           exibirMensagem(
               mensagemProduto,
               data.mensagem || "Não foi possível realizar a compra.",
               "erro"
           );
           return;
       }

       exibirMensagem(
           mensagemProduto,
           "Pedido criado com sucesso!",
           "sucesso"
       );
   } catch (erro) {
       console.error("Erro ao realizar a compra:", erro);

       exibirMensagem(
           mensagemProduto,
           "Não foi possível conectar ao servidor.",
           "erro"
       );
   }
});