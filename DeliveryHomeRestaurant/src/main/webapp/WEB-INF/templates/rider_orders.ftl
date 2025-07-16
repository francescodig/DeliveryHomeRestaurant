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
        
        <!-- Error Section -->
        <#include "error_section.ftl">

        <div class="deliveries-container">
            <h2>Ordini Pronti</h2>
            <#list orders as order>
                <#include "order_card.ftl">
            </#list>
        </div>

        <div class="deliveries-container">
            <h2>Ordini In Consegna</h2>
            <#list ordersOnDelivery as order>
                <#include "order_card.ftl">
            </#list>
        </div>

        <div class="deliveries-container">
            <h2>I miei ordini</h2>
            <#if myOrders?size gt 0>
                <#list myOrders as order>
                    <#include "order_card.ftl">
                </#list>
            <#else>
                <p>Non hai ordini assegnati al momento.</p>
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
