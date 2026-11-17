const token = exigirAutenticacao();
const mensagemPerfil = document.getElementById("mensagemPerfil");
const navPerfil = document.getElementById("navPerfil");

const nome =  document.getElementById("nome");
const email = document.getElementById("email");
const tipoUsuario = document.getElementById("tipoUsuario");

async function carregarPerfil(){
    try{
        const response = await fetchAutenticado("/api/usuarios/me");

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        const dadosUsuario = await response.json();

        if (!response.ok){
            exibirMensagem(
                mensagemPerfil,
                dadosUsuario.mensagem || "Não foi possível carregar o perfil.",
                "erro"
            );
        }

        nome.textContent = dadosUsuario.nome;
        email.textContent = dadosUsuario.email;
        tipoUsuario.textContent = dadosUsuario.tipoUsuario;

        if (dadosUsuario.tipoUsuario === "Administrador") {

            const admRelatorios = document.createElement("a");
            admRelatorios.textContent = "Relatório de Vendas";
            admRelatorios.href = "/admin-relatorios.html";

            const adminProdutos = document.createElement("a");
            adminProdutos.textContent = "Gerenciar Produtos";
            adminProdutos.href = "/admin-produtos.html";

            const adminPedidos = document.createElement("a");
            adminPedidos.textContent = "Gerenciar Pedidos";
            adminPedidos.href = "/admin-pedidos.html";

            navPerfil.appendChild(admRelatorios);
            navPerfil.appendChild(adminProdutos);
            navPerfil.appendChild(adminPedidos);
        }
    } catch (erro) {
        console.error("Erro ao conectar ao servidor:", erro);

        exibirMensagem(
            mensagemPerfil,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
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
        const response = await fetchAutenticado("/api/usuarios/me", {
            method: "DELETE"
        });

        if (!response.ok){
            const data = response.json();

            exibirMensagem(
                mensagemPerfil,
                data.mensagem || "Não foi possível carregar o perfil.",
                "erro"
            );
            return;
        }
    } catch (erro){
        console.error("Erro ao conectar ao sistema.");

        exibirMensagem(
            mensagemPerfil,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
    window.location.href = "/login.html";
});