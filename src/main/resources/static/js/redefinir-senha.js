const formulario = document.getElementById("formRedefinir");
const mensagemRedefinir = document.getElementById("mensagemRedefinir");
const parametros = new URLSearchParams(window.location.search);
const token = parametros.get("token");

if (!token) {
    exibirMensagem(
        mensagemRedefinir,
        "Token de recuperação não informado.",
        "erro"
    );
}

formulario.addEventListener("submit", async function(event){
    event.preventDefault();

    if (!token) {
        return;
    }

    const novaSenha = document.getElementById("novaSenha").value;
    const confirmarSenha = document.getElementById("confirmarSenha").value;

    if (novaSenha !== confirmarSenha){
        exibirMensagem(
            mensagemRedefinir,
            "Os senhas informadas não são iguais.",
            "erro"
        );
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

            exibirMensagem(
                mensagemRedefinir,
                erro.mensagem || "Não foi possível redefinir a senha.",
                "erro"
            );
            return;
        }

        exibirMensagem(
            mensagemRedefinir,
            "Senha redefinida com sucesso!",
            "sucesso"
        );

        setTimeout(function () {
            window.location.href = "/login.html";
        }, 1500);

    } catch (erro) {
        console.error("Erro ao redefinir senha:", erro);

        exibirMensagem(
            mensagemRedefinir,
            "Não foi possível conectar ao servidor.",
            "erro"
        );
    }
});