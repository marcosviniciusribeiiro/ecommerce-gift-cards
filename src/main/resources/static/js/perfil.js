const token = localStorage.getItem("token");
const mensagemPerfil = document.getElementById("mensagemPerfil");
const navPerfil = document.getElementById("navPerfil");

if (!token) {
    window.location.href = "/login.html";
}

const nome =  document.getElementById("nome");
const email = document.getElementById("email");
const tipoUsuario = document.getElementById("tipoUsuario");

async function carregarPerfil(){
    try{
        const response = await fetch("/api/usuarios/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        const dadosUsuario = await response.json();

        if (!response.ok){
            mensagemPerfil.textContent =
                dadosUsuario.mensagem || "Não foi possível carregar o perfil.";
        }

        nome.textContent = dadosUsuario.nome;
        email.textContent = dadosUsuario.email;
        tipoUsuario.textContent = dadosUsuario.tipoUsuario;

        if (dadosUsuario.tipoUsuario === "Administrador") {
            const divAdmin = document.createElement("div");

            const paragrafoProdutos = document.createElement("p");

            const admProdutos = document.createElement("a");
            admProdutos.textContent = "Gerenciar Produtos";
            admProdutos.href = "/admin-produtos.html";

            paragrafoProdutos.appendChild(admProdutos);

            const paragrafoPedidos = document.createElement("p");

            const admPedidos = document.createElement("a");
            admPedidos.textContent = "Gerenciar Pedidos";
            admPedidos.href = "/admin-pedidos.html";

            paragrafoPedidos.appendChild(admPedidos);

            divAdmin.appendChild(paragrafoProdutos);
            divAdmin.appendChild(paragrafoPedidos);

            navPerfil.appendChild(divAdmin);
        }

    } catch (erro) {
        console.error("Erro ao conectar ao servidor:",erro);
        mensagemPerfil.textContent = "Não foi possível conectar ao servidor.";
    }
}

carregarPerfil();

const logoutButton = document.getElementById("logoutButton");

logoutButton.addEventListener("click", function () {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
});

const editButton = document.getElementById("editButton");
editButton.addEventListener("click", function (){
    window.location.href = "/atualizar-conta.html";
});

const deleteButton = document.getElementById("deleteButton");

deleteButton.addEventListener("click", async function (event) {
    event.preventDefault();

    const confirmarExclusao = confirm("Deseja apagar os dados dessa conta?");

    if (!confirmarExclusao){
        return;
    }

    try {
        const response = await fetch("/api/usuarios/me", {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok){
            const data = response.json();
            mensagemPerfil.textContent =
                data.mensagem || "Não foi possível carregar o perfil.";
            return;
        }
    } catch (erro){
        console.error("Erro ao conectar ao sistema.");
        mensagemPerfil.textContent = "Não foi possível conectar ao servidor.";
    }
    window.location.href = "/login.html";
});