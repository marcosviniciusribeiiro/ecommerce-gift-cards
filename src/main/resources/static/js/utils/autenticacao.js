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