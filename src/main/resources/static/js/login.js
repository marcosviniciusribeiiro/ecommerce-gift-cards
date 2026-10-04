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

        const dados = await response.json();

        if (!response.ok){
            exibirMensagem(
                mensagemLogin,
                dados.mensagem || "Email ou senha incorretos.",
                "erro"
            );

            return;
        }

        localStorage.setItem("token", dados.token);
        window.location.href = "/perfil.html";
    }catch (erro) {
        console.error("Erro ao conectar ao servidor: ", erro);

        exibirMensagem(
            mensagemLogin,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});

function exibirMensagem(elemento, mensagem, tipo) {
    elemento.textContent = mensagem;

    elemento.classList.remove(
        "mensagem-sucesso",
        "mensagem-erro"
    );

    if (tipo === "sucesso") {
        elemento.classList.add("mensagem-sucesso");
    } else if (tipo === "erro") {
        elemento.classList.add("mensagem-erro");
    }
}