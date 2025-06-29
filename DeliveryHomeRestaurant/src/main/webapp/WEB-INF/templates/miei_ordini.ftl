<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Menu - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/miei_ordini.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css" />
</head>
<body>

    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main>
        <section class="orders-section">
            <h1>I Miei Ordini</h1>

            <#if orders?has_content && orders?size gt 0>
                <#list orders as order>
                    <div class="order-card">
                        <div class="order-header">
                            <h2>Ordine #${order.getId()?default("N/D")}</h2>
                            <span class="order-date">
                                <#if order.getDataEsecuzione()?has_content>
                                   ${order.getDataEsecuzione()}
                                <#else>
                                    Data non disponibile
                                </#if>
                            </span>
                        </div>
                        <div class="order-items">
                            <p>
                                <strong>Prodotti:</strong>
                                <#if order.getProdotti()?has_content>
                                    <#list order.getProdotti() as product>
                                        ${product.getNome()?default("Senza nome")}<#if product_has_next>, </#if>
                                    </#list>
                                <#else>
                                    Nessun prodotto disponibile
                                </#if>
                            </p>
                            <p><strong>Totale:</strong> €${order.getCosto()?string["0.00"]}</p>
                            <p><strong>Note:</strong> ${order.getNote()?default("-")}</p>
                            <p><strong>Stato:</strong> ${order.getStato()?cap_first?default("Sconosciuto")}</p>
                        </div>
                    </div>
                </#list>
            <#else>
                <p>Non hai ancora effettuato ordini.</p>
            </#if>

        <#if orders?has_content>
            <div class="review-conteiner">
                <a href="/DeliveryHomeRestaurant/User/showReviewForm/" class="btn">Scrivi recensione!</a>
            </div>
        </#if>

        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
