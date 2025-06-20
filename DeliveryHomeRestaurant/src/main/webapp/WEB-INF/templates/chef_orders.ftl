<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consegne - Nome Ristorante</title>
    <link rel="stylesheet" href="/resources/css/personale_consegne.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="/resources/css/layout.css">
</head>
<body>
    <#include "header.ftl">

    <main>
        <div class="deliveries-container">
            <#list orders as order>
                <div class="delivery-card">
                    <div class="delivery-header">
                        <h3>Ordine #${order.id}</h3>

                        <#-- Calcolo della classe stato -->
                        <#assign statoClasse = "">
                        <#switch order.stato>
                            <#case "annullato">
                                <#assign statoClasse = "annullato">
                            <#break>
                            <#case "consegnato">
                                <#assign statoClasse = "consegnato">
                            <#break>
                            <#case "pronto">
                                <#assign statoClasse = "pronto">
                            <#break>
                            <#case "in_preparazione">
                                <#assign statoClasse = "in_preparazione">
                            <#break>
                            <#case "in_attesa">
                                <#assign statoClasse = "errore">
                            <#break>
                            <#default>
                                <#assign statoClasse = "">
                        </#switch>

                        <span class="order-status ${statoClasse?html}">
                            ${order.stato?replace("_", " ")?cap_first}
                        </span>
                    </div>

                    <div class="delivery-info">
                        <p><strong>Note:</strong> ${order.note?html}</p>
                        <p><strong>Data esecuzione:</strong> ${order.dataEsecuzione?string("dd/MM/yyyy HH:mm:ss")}</p>
                        <p><strong>Data ricezione:</strong> ${order.dataRicezione?string("dd/MM/yyyy HH:mm:ss")}</p>
                        <p><strong>Costo totale:</strong> €${order.costo}</p>
                        <p><strong>Prodotti:</strong></p>
                        <ul>
                            <#list order.prodotti as prodotto>
                                <li>${prodotto.nome?html} - ${prodotto.descrizione?html} - €${prodotto.costo}</li>
                            </#list>
                        </ul>
                    </div>

                    <form method="POST" action="/Delivery/Chef/cambiaStatoOrdine" class="status-form">
                        <input type="hidden" name="ordineId" value="${order.id}">
                        <label for="status${order.id}">Modifica stato:</label>
                        <select name="stato" id="status${order.id}" class="status-select">
                            <option value="">-- Seleziona stato --</option>
                            <option value="annullato" <#if statoClasse == "annullato">selected</#if>>Annullato</option>
                            <option value="consegnato" <#if statoClasse == "consegnato">selected</#if>>Consegnato</option>
                            <option value="pronto" <#if statoClasse == "pronto">selected</#if>>Pronto</option>
                            <option value="in_preparazione" <#if statoClasse == "in_preparazione">selected</#if>>In Preparazione</option>
                            <option value="in_attesa" <#if statoClasse == "errore">selected</#if>>In Attesa</option>
                        </select>
                    </form>
                </div>
            </#list>
        </div>
    </main>

    <#include "footer.ftl">

    <!-- Modale per conferma -->
    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <p>Sei sicuro di voler aggiornare lo stato in <strong><span id="modalStatus"></span></strong>?</p>
            <button id="confirmBtn">Conferma</button>
            <button onclick="closeModal()">Annulla</button>
        </div>
    </div>

    <script src="/resources/js/orders.js"></script>
    <script src="/resources/js/hamburger.js"></script>
    <script src="/resources/js/theme.js" defer></script>

</body>
</html>

