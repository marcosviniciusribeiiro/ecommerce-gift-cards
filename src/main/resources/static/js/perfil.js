const token = localStorage.getItem("token");

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

        if (!response.ok){
            console.log("Não foi possível carregar o perfil");
            return;
        }

        const data = await response.json();

        document.getElementById("nome").textContent = data.nome;
        document.getElementById("email").textContent = data.email;
        document.getElementById("tipoUsuario").textContent = data.tipoUsuario;

    } catch (erro) {
        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        if (!response.ok){
            console.log("Erro ao carregar o perfil: ", erro);
        }
    }
}

carregarPerfil();

const logoutButton = document.getElementById("logoutButton");

logoutButton.addEventListener("click", function() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
})