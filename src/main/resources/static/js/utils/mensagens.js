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