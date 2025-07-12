<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consegne - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/personale_consegne.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>
    <#include "header.ftl">

    <main>
        <div class="deliveries-container">
            <#list orders as order>
                <div class="delivery-card">
                    <div class="delivery-header">
                        <h3>Ordine #${order.id}</h3>

                        <#-- Calcolo della classe in base allo stato -->
                        <#assign statoClasse = "">
                        <#if order.stato == "annullato">
                            <#assign statoClasse = "annullato">
                        <#elseif order.stato == "consegnato">
                            <#assign statoClasse = "consegnato">
                        <#elseif order.stato == "pronto">
                            <#assign statoClasse = "pronto">
                        <#elseif order.stato == "in_preparazione">
                            <#assign statoClasse = "in_preparazione">
                        <#elseif order.stato == "in_attesa">
                            <#assign statoClasse = "errore">
                        <#else>
                            <#assign statoClasse = "sconosciuto">
                        </#if>

                        <span class="order-status ${statoClasse?html}">
                            ${order.stato?replace("_", " ")?cap_first}
                        </span>
                    </div>

                    <div class="delivery-info">
                        <p><strong><strong>Note:</strong> 
                            <#if order.note??>
                                ${order.note?html}
                            <#else>
                                <i>Nessuna nota</i>
                            </#if></p>
                        <p><strong>Data esecuzione:</strong> ${order.dataEsecuzione}</p>
                        <p><strong>Data ricezione:</strong> ${order.dataRicezione}</p>
                        <p><strong>Costo totale:</strong> €${order.costo}</p>
                        <p><strong>Prodotti:</strong></p>
                        <ul>
                            <#list order.prodotti as prodotto>
                                <li>${prodotto.nome?html} - ${prodotto.descrizione?html} - €${prodotto.costo}</li>
                            </#list>
                        </ul>
                    </div>

                    <form method="POST" action="${contextPath}/Chef/cambiaStatoOrdine" class="status-form">
                        <input type="hidden" name="ordineId" value="${order.id}">
                        <label for="status${order.id}">Modifica stato:</label>
                        <select name="stato" id="status${order.id}" class="status-select" onchange="this.form.submit()">
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


    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <p>Sei sicuro di voler aggiornare lo stato in <strong><span id="modalStatus"></span></strong>?</p>
            <button id="confirmBtn">Conferma</button>
            <button onclick="closeModal()">Annulla</button>
        </div>
    </div>


    <script src="${contextPath}/resources/Js/orders.js"></script>
    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>


</body>
</html>

