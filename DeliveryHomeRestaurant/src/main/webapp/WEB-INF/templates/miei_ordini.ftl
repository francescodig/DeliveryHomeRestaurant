<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Menu - Nome Ristorante</title>
    <link rel="stylesheet" href="/resources/css/miei_ordini.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="/resources/css/layout.css" />
</head>
<body>

    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main>
        <section class="orders-section">
            <h1>I Miei Ordini</h1>

            <#if orders?size gt 0>
                <#list orders as order>
                    <div class="order-card">
                        <div class="order-header">
                            <h2>Ordine #${order.getId()}</h2>
                            <span class="order-date">
                                <#if order.getDataEsecuzione()?has_content>
                                    ${order.getDataEsecuzione()?string["dd MMMM yyyy"]}
                                <#else>
                                    Data non disponibile
                                </#if>
                            </span>
                        </div>
                        <div class="order-items">
                            <p>
                                <strong>Prodotti:</strong>
                                <#list order.getProdotti() as product; 
                                    # Ciclo con separatore virgola
                                    >
                                    ${product.getNome()}<#if product_has_next>, </#if>
                                </#list>
                            </p>
                            <p><strong>Totale:</strong> €${order.getCosto()}</p>
                            <p><strong>Note:</strong> ${order.getNote()?default("-")}</p>
                            <p><strong>Stato:</strong> ${order.getStato()?cap_first}</p>
                        </div>
                    </div>
                </#list>
            <#else>
                <p>Non hai ancora effettuato ordini.</p>
            </#if>

        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="/resources/js/hamburger.js"></script>
    <script src="/resources/js/theme.js" defer></script>

</body>
</html>
