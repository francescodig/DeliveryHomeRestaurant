<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrello - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/check_order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>

    <!-- Header -->
    <#include "header.ftl" />

    <!-- Main Content -->
    <main>
        <section class="cart-section">
            <div class="cart-container">
                <div class="order-card">
                    <div class="order-header">
                        <h2>Riepilogo ordine</h2>
                    </div>
                    <div class="order-items">
                        <strong>Prodotti:</strong>
                        <ul id="cart-items">
                            <!-- JS inserirà qui i prodotti -->
                        </ul>
                        <p class="total-amount"><strong>Totale:</strong> €<span id="total-amount">0.00</span></p>
                    </div>
                    
                    <!-- Form nascosto per l'invio dei dati -->
                    <form id="cartForm" method="POST" action="/DeliveryHomeRestaurant/Ordine/confirmPayment">
                        <input type="hidden" name="cart_data" id="cartDataInput">
                        <input type="hidden" name="created_at" id="createdAtInput">

                        <!-- Sezione Note -->
                        <div>
                            <label for="note">Inserisci Note</label>
                            <input type="text" name="note" id="note" value="">
                        </div>

                        <!-- Sezione Indirizzi -->
                        <div class="address-section">
                            <h3>Seleziona indirizzo di consegna</h3>
                            <div class="radio-group">
                                <#if indirizzi?size gt 0>
                                    <#list indirizzi as indirizzo>
                                       <#if indirizzo.attivo>
                                            <div class="radio-option">
                                                <input type="radio" id="indirizzo_${indirizzo.id}" name="indirizzo_id" value="${indirizzo.id}" <#if indirizzo?index == 0>checked</#if>>
                                                <label class="radio-label" for="indirizzo_${indirizzo.id}">
                                                    <i class="fas fa-map-marker-alt"></i>
                                                    ${indirizzo.via}, 
                                                    ${indirizzo.civico}, 
                                                    ${indirizzo.citta}
                                                </label>
                                            </div>
                                       </#if>
                                    </#list>
                                <#else>
                                    <p>Nessun indirizzo registrato.</p>
                                </#if>
                                <a href="/DeliveryHomeRestaurant/User/showProfile/" class="add-new-btn btn-link">
                                    <i class="fas fa-plus"></i> Aggiungi nuovo indirizzo
                                </a>
                            </div>
                        </div>

                        <!-- Sezione Carte di Credito -->
                        <div class="credit-cards-section">
                            <h3>Seleziona metodo di pagamento</h3>
                            <div class="radio-group">
                                <#if carte_credito?size gt 0>
                                    <#list carte_credito as carta>
                                       <#if carta.attivo>
                                        <div class="radio-option">
                                            <input type="radio" id="carta_${carta.numeroCarta}" name="numero_carta" value="${carta.numeroCarta}" <#if carta?index == 0>checked</#if>>
                                            <label class="radio-label" for="carta_${carta.numeroCarta}">
                                                <i class="far fa-credit-card"></i>
                                                ${carta.nomeIntestatario}
                                                •••• •••• •••• ${carta.numeroCarta?substring(carta.numeroCarta?length - 4)}
                                                (${carta.dataScadenza})
                                            </label>
                                        </div>
                                       </#if>
                                    </#list>
                                <#else>
                                    <p>Nessuna carta di credito registrata.</p>
                                </#if>
                                <a href="/DeliveryHomeRestaurant/User/showProfile/" class="add-new-btn btn-link">
                                    <i class="fas fa-plus"></i> Aggiungi nuova carta
                                </a>
                            </div>
                        </div>

                        <!-- Sezione Orario Consegna -->
                        <div class="delivery-time-options">
                            <strong>Scegli l'orario di consegna:</strong>
                            <div>
                                <input type="radio" id="default-time" name="delivery_time_option" value="default" checked>
                                <label id="default-time-label" for="default-time">Orario proposto: --:--</label>
                            </div>
                            <div>
                                <input type="radio" id="custom-time" name="delivery_time_option" value="custom">
                                <label for="custom-time">Scegli un altro orario</label>
                                <input type="datetime-local" id="custom-delivery-time" name="custom_delivery_time" style="display: none;">
                            </div>
                        </div>
                        <input type="hidden" name="dataConsegna" id="dataConsegna">

                        <button onclick="submitCart()" id="checkout-button" class="checkout-button">Procedi al pagamento</button>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl" />

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
    <script src="${contextPath}/resources/Js/cart.js"></script>
    <script src="${contextPath}/resources/Js/check_order.js"></script>
</body>
</html>
