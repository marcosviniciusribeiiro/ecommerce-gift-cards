function obterToken() {
    return localStorage.getItem("token");
}

function exigirAutenticacao() {
    const token = obterToken();

    if (!token) {
        window.location.href = "/login.html";
        return null;
    }

    return token;
}

function tratarErroAutenticacao(response, elementoMensagem) {
    if (response.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login.html";
        return true;
    }

    if (response.status === 403) {
        exibirMensagem(
            elementoMensagem,
            "Você não possui permissão para realizar esta ação.",
            "erro"
        );
        return true;
    }

    return false;
}