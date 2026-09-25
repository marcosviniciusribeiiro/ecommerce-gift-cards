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

        if (!response.ok){
            mensagemPerfil.textContent =
                data.mensagem || "Não foi possível carregar o perfil.";
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
    const nome = document.getElementById("nome").textContent;
    const email = document.getElementById("email").textContent;
    alert("Nome: " + nome + ", Email: " + email);
});
// editButton.addEventListener("click", function (){

//     // const response = await fetch("/api/usuarios/me", {
//     //     method: "PUT",
//     //     headers: {
//     //         "Context-Type": "application/json"
//     //     },
//     //     body: JSON.stringify( {
//     //         nome: nome,
//     //         email: email,
//     //         senha: senha
//     //     });
//     // });
//
// });

const deleteButton = document.getElementById("deleteButton");

deleteButton.addEventListener("click", function () {
    alert("ok");
});