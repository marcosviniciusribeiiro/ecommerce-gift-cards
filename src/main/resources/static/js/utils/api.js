async function fetchAutenticado(url, opcoes = {}) {
    const token = obterToken();

    const headers = new Headers(opcoes.headers || {});

    if (token) {
        headers.set("Authorization", `Bearer ${token}`);
    }

    return fetch(url, {
        ...opcoes,
        headers
    });
}