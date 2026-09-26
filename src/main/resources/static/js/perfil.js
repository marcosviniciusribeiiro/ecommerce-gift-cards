const token = localStorage.getItem("token");
const mensagemPerfil = document.getElementById("mensagemPerfil");

if (!token) {
    window.location.href = "/login.html";
}

async function carregarPerfil(){
    try{
        const response = await fetch("/api/usuarios/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })

        const data = await response.json();

        if (!response.ok){
            mensagemPerfil.textContent =
                data.mensagem || "Não foi possível carregar o perfil.";
        }

        document.getElementById("nome").textContent = data.nome;
        document.getElementById("email").textContent = data.email;
        document.getElementById("tipoUsuario").textContent = data.tipoUsuario;

    } catch (erro) {
        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }
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

    confirm("Deseja apagar os dados dessa conta?");

    try {
        const response = await fetch("/api/usuarios/me", {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        const data = response.json();

        if (!response.ok){
            mensagemPerfil.textContent =
                data.mensagem || "Não foi possível atualizar o perfil.";
            return;
        }
    } catch (erro){
        console.error("Erro ao conectar ao sistema.");
    }
    window.location.href = "/login.html";
});