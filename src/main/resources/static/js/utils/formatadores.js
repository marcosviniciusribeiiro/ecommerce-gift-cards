function formatarMoeda(valor){
    return Number(valor).toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });
}

function formatarDataHora(data){
    return new Date(data).toLocaleString("pt-BR", {
        dateStyle: "short",
        timeStyle: "short"
    });
}