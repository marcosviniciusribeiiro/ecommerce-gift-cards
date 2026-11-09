const numeroPedido = document.getElementById("numeroPedido");
const valorPagamento = document.getElementById("valorPagamento");
const statusPagamento = document.getElementById("statusPagamento");

const areaPix = document.getElementById("areaPix");
const imagemQrCode = document.getElementById("imagemQrCode");
const codigoPix = document.getElementById("codigoPix");
const expiracaoPix = document.getElementById("expiracaoPix");

const copiarPixButton = document.getElementById("copiarPixButton");
const mensagemPagamento = document.getElementById("mensagemPagamento");
const voltarPedido = document.getElementById("voltarPedido");

const parametros = new URLSearchParams(window.location.search);
const idPedido = Number(parametros.get("idPedido"));

const token = exigirAutenticacao();

async function carregarPagamento() {
    if (!token) return;

    if (!Number.isSafeInteger(idPedido) || idPedido <= 0) {
        exibirMensagem(
            mensagemPagamento,
            "Identificador do pedido inválido.",
            "erro"
        );
        return;
    }

    numeroPedido.textContent = `Pedido #${idPedido}`;
    voltarPedido.href = `/pedido.html?id=${idPedido}`;

    areaPix.hidden = true;

    try {
        const responsePagamento = await fetchAutenticado(`/api/pagamentos/pedido/${idPedido}/pix`, {
            method: "POST"
        });

        if (tratarErroAutenticacao(responsePagamento, mensagemPagamento)) return;

        if (!responsePagamento.ok) {
            throw new Error("Não foi possível criar ou consultar a cobrança.");
        }

        const pagamento = await responsePagamento.json();

        valorPagamento.textContent = formatarMoeda(pagamento.valor);

        statusPagamento.textContent = pagamento.status;

        if (pagamento.status !== "PENDING") {
            exibirMensagem(
                mensagemPagamento,
                "Esta cobrança não está pendente.",
                "erro"
            );
            return;
        }

        // Consulta os dados Pix do pagamento.
        const responsePix = await fetchAutenticado(`/api/pagamentos/${pagamento.idPagamento}/pix/qrcode`);

        if (tratarErroAutenticacao(responsePix, mensagemPagamento)) return;

        if (!responsePix.ok) {
            throw new Error("Não foi possível consultar o QR Code Pix.");
        }

        const pix = await responsePix.json();

        if (!pix.success || !pix.encodedImage || !pix.payload) {
            throw new Error("O Asaas não retornou os dados completos do Pix.");
        }

        imagemQrCode.src = `data:image/png;base64,${pix.encodedImage}`;

        codigoPix.value = pix.payload;

        expiracaoPix.textContent = formatarDataHora(pix.expirationDate) || "Não informada";

        areaPix.hidden = false;

        exibirMensagem(
            mensagemPagamento,
            "Cobrança Pix gerada. Aguardando pagamento.",
            "sucesso"
        );

    } catch (erro) {
        console.error("Erro ao carregar pagamento:", erro);

        exibirMensagem(
            mensagemPagamento,
            erro.message || "Erro ao carregar pagamento.",
            "erro"
        );
    }
}

copiarPixButton.addEventListener("click", async function () {
    if (!codigoPix.value) return;

    try {
        await navigator.clipboard.writeText(codigoPix.value);

        exibirMensagem(
            mensagemPagamento,
            "Código Pix copiado com sucesso!",
            "sucesso"
        );

    } catch (erro) {
        console.error("Erro ao copiar código Pix:", erro);

        exibirMensagem(
            mensagemPagamento,
            "Não foi possível copiar o código Pix.",
            "erro"
        );
    }
});

carregarPagamento();