<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Order - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/order.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css" />
</head>
<body>
    <#include "header.ftl">

    <main>
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <section class="menu-section">
            <h1>Ordina dal menù</h1>

            <#list menu as categoria>
                <div class="menu-category">
                    <h2>
                        <#if categoria.categoria == "PIZZE"><i class="fas fa-pizza-slice"></i></#if>
                        <#if categoria.categoria == "CALZONI"><i class="fas fa-bread-slice"></i></#if>
                        <#if categoria.categoria == "CONTORNI"><i class="fas fa-carrot"></i></#if>
                        <#if categoria.categoria == "BEVANDE"><i class="fas fa-glass-whiskey"></i></#if>
                        ${categoria.categoria?html}
                    </h2>
                    <div class="menu-items">
                        <#list categoria.piatti as piatto>
                            <div class="menu-item">
                                <div class="item-info">
                                    <h3>${piatto.nome?html}</h3>
                                    <p>${piatto.descrizione?html}</p>
                                </div>
                                <div class="item-price">€${piatto.costo?html}</div>
                                <button class="add-button" data-id="${piatto.id}">+</button>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
        </section>
    </main>

    <div id="cart-icon" class="cart-icon hidden">
        <i class="fas fa-shopping-cart"></i>
        <span id="cart-badge">0</span>
    </div>

    <div id="cart" class="cart">
        <h2>Il tuo ordine</h2>
        <ul id="cart-items"></ul>
        <p id="cart-total">Totale: €0.00</p>
        <a href="checkout.html"><button>Prosegui</button></a>
    </div>

    <div id="product-modal" class="modal hidden">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <div id="modal-body"></div>
        </div>
    </div>

    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>

<script src="${contextPath}/resources/Js/cart.js" defer></script>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        cart = localStorage.getItem("cart") ? JSON.parse(localStorage.getItem("cart")) : [];
        renderCart();
        showCartIcon();
    });
</script>
</html>
