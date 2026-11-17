const formFiltro = document.getElementById("formFiltroRelatorio");
const inputInicio = document.getElementById("dataInicio");
const inputFim = document.getElementById("dataFim");
const limparFiltroButton = document.getElementById("limparFiltro");

const totalPedidosPagos = document.getElementById("totalPedidosPagos");
const totalGiftCardsVendidos = document.getElementById("totalGiftCardsVendidos");
const faturamentoTotal = document.getElementById("faturamentoTotal");

const tabelaProdutos = document.getElementById("tabelaProdutosVendidos");
const mensagemRelatorio = document.getElementById("mensagemRelatorio");

const URL_RELATORIOS = "/api/admin/relatorios";

const token = exigirAutenticacao();

const moeda = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL"
});

let ultimaConsulta = 0;

function limparMensagemRelatorio() {
    mensagemRelatorio.textContent = "";
    mensagemRelatorio.className = "mensagem";
}

function mostrarErroRelatorio(mensagem) {
    mensagemRelatorio.textContent = mensagem;
    mensagemRelatorio.className = "mensagem mensagem-erro";
}

function obterParametrosPeriodo() {
    const parametros = new URLSearchParams();

    if (inputInicio.value) {
        parametros.append("inicio", inputInicio.value);
    }
    if (inputFim.value) {
        parametros.append("fim", inputFim.value);
    }

    const query = parametros.toString();

    return query ? `?${query}` : "";
}

async function consultarRelatorio(url){
    const resposta = await fetchAutenticado(url);

    if (!resposta.ok) {
        let mensagem = "Erro ao consultar relatório.";

        try {
            const erro = await resposta.json();
            mensagem = erro.mensagem || mensagem;
        } catch{

        }

        if (resposta.status === 403) {
            document.getElementById("conteudoRelatorio").hidden = true;
            mensagem = "Você não tem permissão para acessar os relatórios.";
        }

        throw new Error(mensagem);
    }

    return resposta.json();
}

function  atualizarResumo(resumo) {
    totalPedidosPagos.textContent = resumo.totalPedidosPagos;
    totalGiftCardsVendidos.textContent = resumo.totalGiftCardsVendidos;
    faturamentoTotal.textContent = moeda.format(resumo.faturamentoTotal);
}

function atualizarProdutos(produtos) {
    tabelaProdutos.replaceChildren();

    if (produtos.length === 0) {
        const linha = document.createElement("tr");
        const coluna = document.createElement("td");

        coluna.colSpan = 3;
        coluna.textContent = "Nenhuma venda encontrada para o período.";

        linha.appendChild(coluna);
        tabelaProdutos.appendChild(linha);
        return;
    }

    produtos.forEach(produto => {
        const linha = document.createElement("tr");

        const nome = document.createElement("td");
        nome.textContent = produto.nomeProduto;
        linha.appendChild(nome);

        const quantidade = document.createElement("td");
        quantidade.textContent = produto.quantidadeVendida;
        linha.appendChild(quantidade);

        const faturamento = document.createElement("td");
        faturamento.textContent = moeda.format(produto.faturamento);
        linha.appendChild(faturamento);

        tabelaProdutos.appendChild(linha);
    });
}

function limparResultados() {
    totalPedidosPagos.textContent = "—";
    totalGiftCardsVendidos.textContent = "—";
    faturamentoTotal.textContent = "—";
    tabelaProdutos.replaceChildren();
}

async function carregarRelatorios() {
    const consultaAtual = ++ultimaConsulta;

    limparMensagemRelatorio();
    limparResultados();

    const parametros = obterParametrosPeriodo();

    try {
        const [resumo, produtos] = await Promise.all([
            consultarRelatorio(
                `${URL_RELATORIOS}/resumo${parametros}`
            ),
            consultarRelatorio(
                `${URL_RELATORIOS}/produtos-mais-vendidos${parametros}`
            )
        ]);

        if (consultaAtual !== ultimaConsulta) return;

        atualizarResumo(resumo);
        atualizarProdutos(produtos);

    } catch (erro) {
        if (consultaAtual !== ultimaConsulta) return;

        mostrarErroRelatorio(erro.message);
    }
}

formFiltro.addEventListener("submit", async (event) => {
    event.preventDefault();

    const inicio = inputInicio.value;
    const fim = inputFim.value;

    if (inicio && fim && inicio > fim) {
        mostrarErroRelatorio(
            "A data inicial não pode ser posterior à data final."
        );
        return;
    }

    await carregarRelatorios();
});

limparFiltroButton.addEventListener("click", async () => {
    formFiltro.reset();

    await carregarRelatorios();
});

document.addEventListener("DOMContentLoaded", async () => {
    await carregarRelatorios();
});