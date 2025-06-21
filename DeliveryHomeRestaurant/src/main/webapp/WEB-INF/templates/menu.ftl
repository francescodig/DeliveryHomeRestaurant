<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/menu.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <section class="menu-section">
            <h1>Menù</h1>

            <#list menu as categoria>
                <div class="menu-category">
                    <h2>${categoria.categoria?html}</h2>
                    <div class="menu-items">
                        <#list categoria.piatti as piatto>
                            <div class="menu-item">
                                <div class="item-info">
                                    <h3>${piatto.nome?html}</h3>
                                    <p>${piatto.descrizione?html}</p>
                                </div>
                                <div class="item-price">€${piatto.costo?html}</div>
                            </div>
                        </#list>
                    </div>
                </div>
            </#list>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>
