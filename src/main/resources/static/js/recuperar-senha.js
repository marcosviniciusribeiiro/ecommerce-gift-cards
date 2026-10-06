const formulario = document.getElementById("formRecuperar");
const mensagemRecuperar = document.getElementById("mensagemRecuperar");

formulario.addEventListener("submit", async function(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;

    try {
        const response = await fetch("/api/usuarios/recuperar-senha", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email
            })
        });

        const dadosRecuperacao = await response.json();

        if (!response.ok) {
            exibirMensagem(
                mensagemRecuperar,
                dadosRecuperacao.mensagem || "Usuário não encontrado.",
                "erro"
            );
            return;
        }

        window.location.href = "/redefinir-senha.html?token=" + encodeURIComponent(dadosRecuperacao.token);
    } catch (erro) {
        console.error("Erro ao enviar o formulário:", erro);

        exibirMensagem(
            mensagemRecuperar,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});