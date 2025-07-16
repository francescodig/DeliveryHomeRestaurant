<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Nome Ristorante</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Section -->   
    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <!-- Login Section -->
        <section class="login-section">
            <div class="login-content">
                <div class="login-image">
                    <img src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-1.2.1&amp;auto=format&amp;fit=crop&amp;w=1350&amp;q=80" alt="Login Image">
                </div>
                <div class="login-form">
                    <h1>Accedi al tuo Account</h1>

                    <!-- Error Section -->
                    <#include "error_section.ftl">

                    <form action="/DeliveryHomeRestaurant/User/loginUser" method="POST">
                        <div class="form-group">
                            <label for="username">Nome Utente</label>
                            <input 
                                type="text" 
                                id="username" 
                                name="username" 
                                required
                                value="${(username)!''?html}"
                            >
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input 
                                type="password" 
                                id="password" 
                                name="password" 
                                required
                                value="${(password)!''?html}"
                            >
                        </div>
                        <div class="form-group remember-me">
                            <input type="checkbox" id="rememberMe" name="rememberMe" value="1"
                                <#if rememberMe?? && rememberMe == "1">checked</#if>
                            >
                            <label for="rememberMe">Ricordami</label>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn">Accedi</button>
                        </div>
                        <div class="form-group">
                            <p>Non hai un account? <a href="/DeliveryHomeRestaurant/User/showRegisterForm">Registrati</a></p>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>
