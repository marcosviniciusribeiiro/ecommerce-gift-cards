const formulario = document.getElementById("cadastroProdutoForm");
const mensagemCadastro = document.getElementById("mensagemCadastro");
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "/login.html";
}

formulario.addEventListener("submit", async function (event) {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const descricao = document.getElementById("descricao").value;
    const plataforma = document.getElementById("plataforma").value;
    const valor = document.getElementById("valor").value;

    try {
        const response = await fetch("/api/produtos/cadastro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nome: nome,
                descricao: descricao,
                plataforma: plataforma,
                valor: Number(valor)
            })
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
            return;
        }

        if (response.status === 403) {
            mensagemCadastro.textContent = "Você não possui permissão para cadastrar produtos.";
            return;
        }

        if (!response.ok) {
            const erro = await response.json();
            mensagemCadastro.textContent = erro.mensagem || "Não foi possível cadastrar o produto.";
            return;
        }

        mensagemCadastro.textContent = "Produto cadastrado com sucesso!";

        // formulario.reset();
    } catch (erro) {
        console.error("Erro ao cadastrar o produto:", erro);
        mensagemCadastro.textContent = "Não foi possível conectar ao servidor.";
    }
})