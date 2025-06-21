<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Restaurant - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/account.css">
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

        <section class="account-container">
            <h2>Gestione Account</h2>

            <!-- Modifica Profilo -->
            <div class="password-section">
                <h3>Modifica Profilo</h3>
                <form action="/Delivery/User/modifyProfile" method="POST">
                    <div class="form-group">
                        <label for="newName">Nuovo Nome</label>
                        <input id="newName" name="newName" value="${utente.nome}" required>
                    </div>

                    <div class="form-group">
                        <label for="newSurname">Nuovo Cognome</label>
                        <input id="newSurname" name="newSurname" value="${utente.cognome}" required>
                    </div>

                    <button type="submit" class="btn-submit">Aggiorna Profilo</button>
                </form>
            </div>

            <!-- Cambia Password -->
            <div class="password-section">
                <h3>Cambia Password</h3>
                <form action="/Delivery/User/changePassword" method="POST">
                    <div class="form-group">
                        <label for="oldPassword">Vecchia Password</label>
                        <input type="password" id="oldPassword" name="oldPassword" required>
                    </div>

                    <div class="form-group">
                        <label for="newPassword">Nuova Password</label>
                        <input type="password" id="newPassword" name="newPassword" required>
                    </div>

                    <button type="submit" class="btn-submit">Aggiorna Password</button>
                </form>
            </div>

            <!-- Link ai miei ordini -->
            <div class="orders-link">
                <a href="/Delivery/User/showMyOrders/" class="btn-link">
                    <i class="fas fa-box-open"></i> I miei ordini
                </a>
            </div>

            <!-- Logout -->
            <div class="logout-section">
                <a href="/Delivery/User/logoutUser/" class="btn-logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>
