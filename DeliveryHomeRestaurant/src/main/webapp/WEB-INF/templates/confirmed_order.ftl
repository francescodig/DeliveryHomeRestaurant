<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conferma Ordine</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/confirm.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

</head>
<body>

    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main>
        <section>
            <div>
                <h1>Ordine Confermato</h1>
                <p>Il suo ordine è andato a buon fine! Grazie per aver scelto noi.</p>
                <a class="button" href="/DeliveryHomeRestaurant/User/home">Torna alla Homepage</a>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            localStorage.removeItem("cart");
            localStorage.removeItem("cart_createdAt");
        });
    </script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
