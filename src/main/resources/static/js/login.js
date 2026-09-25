const formulario = document.getElementById("loginForm");
const mensagemLogin = document.getElementById("mensagemLogin");

formulario.addEventListener("submit", async function (event){
    event.preventDefault();
    
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    try{
        const response = await fetch("/api/usuarios/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                senha: senha
            })
        });

        const data = await response.json();

        if (!response.ok){
            mensagemLogin.textContent = data.mensagem;
            return;
        }

        localStorage.setItem("token", data.token);
        window.location.href = "/perfil.html";
    }catch (erro) {
        console.error("Erro ao conectar ao servidor: ", erro);
        mensagemLogin.textContent = "Não foi possível conectar ao servidor";
    }
});