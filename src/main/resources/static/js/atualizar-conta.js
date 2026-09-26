const formulario = document.getElementById("atualizarForm");
const mensagemAtualizar = document.getElementById("mensagemAtualizar");
const token = localStorage.getItem("token");

formulario.addEventListener("submit", async function (event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    try {
        const response = await fetch("/api/usuarios/me", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nome: nome,
                email: email,
                senha: senha
            })
        })

        const data = await response.json();

        if (!response.ok){
            mensagemAtualizar.textContent =
                data.mensagem || "Não foi possível atualizar o perfil.";
            return;
        }

        localStorage.removeItem("token");
        window.location.href = "/login.html";
    } catch (erro){
        console.error("Não foi possivel conectar ao servidor.");
    }
});