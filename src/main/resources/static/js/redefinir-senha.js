const formulario = document.getElementById("formRedefinir");
const mensagemRedefinir = document.getElementById("mensagemRedefinir");
const parametros = new URLSearchParams(window.location.search);
const token = parametros.get("token");

if (!token) {
    mensagemRedefinir.textContent =
        "Token de recuperação não informado.";
}

formulario.addEventListener("submit", async function(event){
    event.preventDefault();

    if (!token) {
        return;
    }

    const novaSenha = document.getElementById("novaSenha").value;
    const confirmarSenha = document.getElementById("confirmarSenha").value;

    if (novaSenha !== confirmarSenha){
        mensagemRedefinir.textContent = "Os senhas informadas não são iguais.";
        return;
    }

    try {
        const response = await fetch("/api/usuarios/redefinir-senha", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                token: token,
                novaSenha: novaSenha
            })
        });

        if (!response.ok) {
            const erro = await response.json();

            mensagemRedefinir.textContent = erro.mensagem || "Não foi possível redefinir a senha.";
            return;
        }

        mensagemRedefinir.textContent = "Senha redefinida com sucesso!";

        setTimeout(function () {
            window.location.href = "/login.html";
        }, 1500);

    } catch (erro) {
        console.error("Erro ao redefinir senha:", erro);
        mensagemRedefinir.textContent = "Não foi possível conectar ao servidor.";
    }
});
