<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/miei_ordini.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>

    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main>

        <!-- Orders Section -->
        <section class="orders-section">
            <h1>I Miei Ordini</h1>

            <#if orders?has_content>
                <#list orders as order>
                    <div class="order-card">
                        <div class="order-header">
                            <h2>Ordine del ${order.getDataRicezione()}</h2>
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
                                <#list order.getItemOrdini() as itemOrdine>
                                    ${itemOrdine.getProdotto().getNome()}<#if itemOrdine_has_next>, </#if>
                                </#list>
                            </p>
                            <p><strong>Totale:</strong> €${order.getCosto()?string["0.00"]}</p>
                            <p><strong>Note:</strong> ${order.getNote()?default("-")}</p>
                            <p><strong>Stato:</strong> ${order.getStato()?cap_first}</p>
                            <p><strong>Indirizzo:</strong> ${order.getIndirizzoConsegna().getCitta()}, ${order.getIndirizzoConsegna().getVia()}, ${order.getIndirizzoConsegna().getCivico()}</p>
                            <p><strong>Metodo Pagamento:</strong> ${order.getCartaPagamento().getNomeCarta()}</p>
                        </div>


                    </div>
                </#list>
            <#else>
                <p>Non hai ancora effettuato ordini.</p>
            </#if>

        </section>

        <#if orders?has_content>
            <div class="review-conteiner">
                <a href="/DeliveryHomeRestaurant/User/showReviewForm/" class="btn">Scrivi recensione!</a>
            </div>
        </#if>

    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <!-- Modal Aggiungi Segnalazione -->
    <div id="reportModal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>

            <h2>Crea segnalazione <span id="modal-order-id"></span></h2>

            <form method="POST" action="/DeliveryHomeRestaurant/Segnalazione/writeReport" class="form">

                <div class="form-group">
                    <label for="descrizione">Descrizione</label>
                    <input type="text" id="descrizione" name="descrizione" required>
                </div>

                <div class="form-group">
                    <label for="testo">Testo</label>
                    <input type="text" id="testo" name="testo" required>
                </div>

                <input type="hidden" id="ordine_id" name="ordine_id" required>

                <button type="submit">Invia Segnalazione</button>

            </form>
        </div>
    </div>

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
    <script src="${contextPath}/resources/Js/report_modal.js"></script>
</body>
</html>
