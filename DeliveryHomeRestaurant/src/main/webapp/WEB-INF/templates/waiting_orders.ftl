<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consegne - Nome Ristorante</title>
    <link rel="icon" type="image/x-icon" href="${contextPath}/resources/Immagini/favicon.ico">
    <link rel="stylesheet" href="${contextPath}/resources/css/personale_consegne.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>
    <#include "header.ftl">

    <main>
        <div class="deliveries-container">
            <!-- Error Section -->
            <#include "error_section.ftl">


            <#if orders?has_content>
                <#list orders as order>
                    <div class="delivery-card">
                        <div class="delivery-header">
                            <h3>Ordine #${order.id}</h3>

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
                            </#if>

                            <span class="order-status ${statoClasse}">${order.stato?replace("_", " ")?capitalize}</span>
                        </div>
                        <div class="delivery-info">
                            <p><strong>Note:</strong> ${order.getNote()?default("-")}</p>
                            <p><strong>Data esecuzione:</strong> ${order.dataEsecuzione}</p>
                            <p><strong>Data consegna prevista:</strong> ${order.dataRicezione}</p>
                            <p><strong>Indirizzo :</strong> ${order.indirizzoConsegna.via} ${order.indirizzoConsegna.civico}, ${order.indirizzoConsegna.citta}</p>
                            <p><strong>Costo totale:</strong> €${order.costo}</p>
                            <p><strong>Prodotti:</strong></p>
                            <ul>
                                <#list order.itemOrdini as itemOrdine>
                                    <li class="product-item">
                                        ${itemOrdine.prodotto.nome?html} - ${itemOrdine.prodotto.descrizione?html} - qty: ${itemOrdine.quantita} - €${itemOrdine.prezzoUnitarioAlMomento}
                                    </li>
                                </#list>
                            </ul>
                        </div>
                        <div class="delivery-actions">
                            <form action="/DeliveryHomeRestaurant/Chef/accettaOrdine" method="POST" style="display:inline-block; margin-right: 10px;">
                                <input type="hidden" name="ordine_id" value="${order.id}" />
                                <button type="submit" class="btn btn-success">Accetta</button>
                            </form>

                            <form action="/DeliveryHomeRestaurant/Chef/rifiutaOrdine" method="POST" style="display:inline-block;">
                                <input type="hidden" name="ordine_id" value="${order.id}" />
                                <textarea name="motivazione_rifiuto" placeholder="Motivazione rifiuto" rows="2" cols="30" required style="resize: none; margin-bottom: 5px;"></textarea>
                                <br>
                                <button type="submit" class="btn btn-danger">Rifiuta</button>
                            </form>
                        </div>
                    </div>
                </#list>
            <#else>
                <p>Nessun ordine in attesa.</p>
            </#if>
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

    <script src="${contextPath}/resources/Js/orders.js"></script>
    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
