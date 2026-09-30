const formulario = document.getElementById("cadastroForm");
const mensagemCadastro = document.getElementById("mensagemCadastro");

formulario.addEventListener("submit", async function(event){
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    try {
        const response = await fetch("/api/usuarios/cadastro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                nome: nome,
                email: email,
                senha: senha
            })
        });

        const data = await response.json();

        if(!response.ok){
            mensagemCadastro.textContent = data.mensagem || "Não foi possível realizar o cadastro.";
            return;
        }

        window.location.href = "/login.html";
    } catch (erro){
        console.error("Erro ao conectar ao servidor:", erro);
        mensagemCadastro.textContent = "Não foi possível conectar ao servidor.";
    }
});