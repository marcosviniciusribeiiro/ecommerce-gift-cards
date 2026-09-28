const mensagemProduto = document.getElementById("mensagemProduto");
const parametros = new URLSearchParams (window.location.search);
const idProduto = parametros.get("id");
const comprarButton = document.getElementById("comprarButton");

async function carregarProduto() {
    if (!idProduto) {
        mensagemProduto.textContent = "Produto não informado."
        return;
    }

    try {
        const response = await fetch("/api/produtos/" + idProduto);

        if (!response.ok) {
            mensagemProduto.textContent = "Não foi possível encontrar o produto."
            return;
        }

        const produto = await response.json();

        document.getElementById("nomeProduto").textContent = produto.nome;
        document.getElementById("descricaoProduto").textContent = produto.descricao;
        document.getElementById("plataformaProduto").textContent = produto.plataforma;
        document.getElementById("valorProduto").textContent = "R$ " + Number(produto.valor).toFixed(2);
    } catch (erro) {
        console.error("Erro ao carregar o produto:", erro);

        mensagemProduto.textContent = "Não foi possível conectar ao servidor."

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
           mensagemProduto.textContent = data.mensagem || "Não foi possível realizar a compra.";
           return;
       }
       console.log("Pedido criado:", data);
       mensagemProduto.textContent = "Pedido criado com sucesso!";
   } catch (erro) {
       console.error("Erro ao realizar a compra:", erro);
       mensagemProduto.textContent = "Não foi possível conectar ao servidor."
   }
});