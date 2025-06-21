<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Home Restaurant - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/css/checkout.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="${contextPath}/css/layout.css" />
    <script src="${contextPath}/Js/cart.js"></script>
</head>
<body>
    <#-- Include header -->
    <#include "header.ftl" />

    <main>
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <section class="menu-section">
            <h1>Riepilogo Ordine</h1>

            <div class="checkout-grid">
                <!-- Riepilogo Ordine (carrello gestito da JS) -->
                <section class="order-summary">
                    <h2><i class="fas fa-receipt"></i> Il tuo ordine</h2>
                    <ul class="order-items" id="order-items"></ul>
                    <div class="order-totals">
                        <div class="subtotal">
                            <span>Subtotale:</span>
                            <span>€35.50</span>
                        </div>
                        <div class="delivery">
                            <span>Spedizione:</span>
                            <span>€3.50</span>
                        </div>
                        <div class="total">
                            <span>Totale:</span>
                            <span>€39.00</span>
                        </div>
                    </div>
                </section>

                <!-- Metodo di Pagamento -->
                <section class="payment-method">
                    <h2><i class="fas fa-credit-card"></i> Metodo di pagamento</h2>

                    <div class="saved-cards">
                        <#list savedCards as card>
                            <div class="card-option <#if card?index == 0>selected</#if>">
                                <input type="radio" name="payment" id="card${card.id}" <#if card?index == 0>checked</#if>>
                                <label for="card${card.id}">
                                    <i class="fab fa-cc-${card.brand?lower_case}"></i>
                                    <span>${card.brand} **** ${card.last4}</span>
                                    <span>Scadenza: ${card.expiry}</span>
                                </label>
                            </div>
                        </#list>
                    </div>

                    <button class="add-new" id="add-payment">
                        <i class="fas fa-plus"></i> Aggiungi nuovo metodo di pagamento
                    </button>

                    <div class="new-card-form hidden">
                        <h3>Inserisci nuova carta</h3>
                        <div class="form-group">
                            <label for="card-number">Numero Carta</label>
                            <input type="text" id="card-number" placeholder="1234 5678 9012 3456" />
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="card-expiry">Scadenza</label>
                                <input type="text" id="card-expiry" placeholder="MM/AA" />
                            </div>
                            <div class="form-group">
                                <label for="card-cvc">CVC</label>
                                <input type="text" id="card-cvc" placeholder="123" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="card-name">Nome sulla carta</label>
                            <input type="text" id="card-name" placeholder="Mario Rossi" />
                        </div>
                        <button class="save-card">Salva carta</button>
                    </div>
                </section>

                <!-- Indirizzo di Consegna -->
                <section class="delivery-address">
                    <h2><i class="fas fa-map-marker-alt"></i> Indirizzo di consegna</h2>

                    <div class="saved-addresses">
                        <#list savedAddresses as address>
                            <div class="address-option <#if address?index == 0>selected</#if>">
                                <input type="radio" name="address" id="address${address.id}" <#if address?index == 0>checked</#if>>
                                <label for="address${address.id}">
                                    <span class="address-title">${address.title}</span>
                                    <span>${address.street}, ${address.zip}, ${address.city}</span>
                                    <span>${address.notes}</span>
                                </label>
                            </div>
                        </#list>
                    </div>

                    <button class="add-new" id="add-address">
                        <i class="fas fa-plus"></i> Aggiungi nuovo indirizzo
                    </button>

                    <div class="new-address-form hidden">
                        <h3>Inserisci nuovo indirizzo</h3>
                        <div class="form-group">
                            <label for="address-name">Nome indirizzo (es. Casa, Ufficio)</label>
                            <input type="text" id="address-name" placeholder="Casa" />
                        </div>
                        <div class="form-group">
                            <label for="address-street">Via e numero civico</label>
                            <input type="text" id="address-street" placeholder="Via Roma 123" />
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="address-zip">CAP</label>
                                <input type="text" id="address-zip" placeholder="00100" />
                            </div>
                            <div class="form-group">
                                <label for="address-city">Città</label>
                                <input type="text" id="address-city" placeholder="Roma" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address-notes">Altre informazioni</label>
                            <input type="text" id="address-notes" placeholder="Piano, citofono, ecc." />
                        </div>
                        <button class="save-address">Salva indirizzo</button>
                    </div>
                </section>
            </div>

            <!-- Conferma Ordine -->
            <section class="order-confirmation">
                <div class="delivery-time-options">
                    <h2><i class="fas fa-clock"></i> Seleziona orario di consegna</h2>

                    <div class="time-option">
                        <input type="radio" id="asap" name="delivery-time" value="asap" checked />
                        <label for="asap">Il prima possibile (30-45 minuti)</label>
                    </div>

                    <div class="time-option">
                        <input type="radio" id="schedule" name="delivery-time" value="schedule" />
                        <label for="schedule">Scegli orario</label>
                        <input type="time" id="scheduled-time" name="scheduled-time" disabled />
                    </div>
                </div>

                <button class="confirm-order">Conferma ordine</button>
            </section>
        </section>
    </main>

    <#-- Include footer -->
    <#include "footer.ftl" />

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>

<script src="${contextPath}/resourcers/Js/checkout.js" defer></script>
<script>
document.addEventListener("DOMContentLoaded", function() {
    let cart = localStorage.getItem("cart") ? JSON.parse(localStorage.getItem("cart")) : [];
    let cartListElement = document.getElementById("order-items");

    cart.forEach(item => {
        const li = document.createElement("li");
        const nameQtySpan = document.createElement("span");
        nameQtySpan.textContent = `${item.name} x${item.qty}`;
        const priceSpan = document.createElement("span");
        priceSpan.textContent = `€${(item.price * item.qty).toFixed(2)}`;
        li.appendChild(nameQtySpan);
        li.appendChild(priceSpan);
        cartListElement.appendChild(li);
    });
});
</script>
</html>
