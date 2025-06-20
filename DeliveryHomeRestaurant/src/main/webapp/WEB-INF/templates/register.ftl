<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrati - Nome Ristorante</title>
    <link rel="stylesheet" href="/resources/css/registrati.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="/resources/css/layout.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl">

    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <!-- Registration Section -->
        <section class="login-section">
            <div class="login-content">
                <div class="login-form">
                    <h1>Registrati</h1>

                    <#if error??>
                        <div class="error-message">${error}</div>
                    </#if>

                    <form action="/Delivery/User/registerUser" method="POST">
                        <div class="form-group">
                            <label for="nome">Nome</label>
                            <input 
                                type="text" 
                                id="nome" 
                                name="nome" 
                                required 
                                value="${name!''?html}"
                            >
                        </div>
                        <div class="form-group">
                            <label for="cognome">Cognome</label>
                            <input 
                                type="text" 
                                id="cognome" 
                                name="cognome" 
                                required 
                                value="${cognome!''?html}"
                            >
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input 
                                type="email" 
                                id="email" 
                                name="email" 
                                required 
                                value="${email!''?html}"
                            >
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input 
                                type="password" 
                                id="password" 
                                name="password" 
                                required
                            >
                        </div>
                        
                        <div class="form-group">
                            <label for="role">Ruolo</label>
                            <input 
                                type="text" 
                                id="role" 
                                name="role"  
                                required 
                                value="${role!'cliente'?html}"
                            >
                            <small>Inserisci cliente per prova</small>
                        </div>
                        
                        <div class="form-group">
                            <button type="submit" class="btn">Registrati</button>
                        </div>
                        <div class="form-group">
                            <p>Sei già registrato? <a href="/Delivery/User/showLoginForm">Accedi</a></p>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="/resources/js/hamburger.js"></script>
    <script src="/resources/js/theme.js" defer></script>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            var roleInput = document.getElementById('role');
            roleInput.addEventListener('input', function(e) {
                // Rimuove caratteri non numerici e limita a 10 cifre
                var val = e.target.value.replace(/\D/g, '').slice(0, 10);
                
                if (val.length <= 3) {
                    e.target.value = val;
                } else if (val.length <= 6) {
                    e.target.value = val.replace(/(\d{3})(\d{0,3})/, '$1 $2');
                } else {
                    e.target.value = val.replace(/(\d{3})(\d{3})(\d{0,4})/, '$1 $2 $3');
                }
            });
        });
    </script>

</body>
</html>

