<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Restaurant - Nome Ristorante</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/home.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">


</head>
<body>

    <!-- Header -->
    <#include "header.ftl" />

    <!-- Main Content -->
    <main>
        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <!-- Navigation Tabs -->
        <section class="tabs">
            <a class="tab" href="#storia">Storia</a>
            <a class="tab" href="#posizione">Dove siamo?</a>
            <a class="tab" href="#recensioni">Recensioni</a>
            <a class="tab" href="#contatti">Contatti</a>
        </section>

        <!-- Story Section -->
        <section id="storia" class="story-section">
            <div class="storia-container">
                <div class="storia-text">
                    <h2>Storia del ristorante</h2>
                    <p>
                        Tutto è partito da una semplice idea:
                        tre soci, una passione per la pizza e la voglia di creare qualcosa di nostro. Poi &egrave; arrivato il Covid. Le restrizioni ci hanno costretti a rivedere i piani: niente sala, solo consegne.
                        Cos&igrave; è nata Home Restaurant, una pizzeria pensata per portare la qualità a casa, quando uscire non era possibile.
                    </p><br>
                    <p>
                        Da l&igrave; non ci siamo pi&ugrave; fermati!
                    </p>
                </div>
                <div class="storia-image">
                    <img src="${contextPath}/resources/Immagini/story.jpeg" alt="Storia del ristorante">
                </div>
            </div>
        </section>

        <!-- Position Section -->
        <section id="posizione" class="position-section">
            <div class="position-container">
                <div class="position-image">
                    <img src="${contextPath}/resources/Immagini/position.png" alt="Storia del ristorante">
                </div>
                <div class="position-text">
                    <h2>Dove siamo?</h2>
                    <p>
                        Ci troviamo nel pieno centro storico dell'Aquila, tra pietra, storia e movida. Una zona che vive di incontri, sapori e serate che iniziano con una pizza e finiscono con una storia da raccontare.
                        Se passi da qui, lo capisci subito: sei nel posto giusto!
                    </p>
                </div>
            </div>
        </section>

        <!-- Reviews Section -->
        <section id="recensioni" class="reviews">
            <h2>Recensioni</h2>
            <div class="review-grid">
                <#list reviews as review>
                    <div class="review-card">
                        <div class="reviewer-info">
                            <div class="reviewer-details">
                                <h3>
                                    Valutazione: ${review.voto}
                                    <i class="fas fa-star" style="color: gold; margin-left: 5px;"></i>
                                </h3>
                            </div>
                        </div>
                        <p class="review-text">"${review.descrizione?html}"</p>
                        <span class="review-date">
                            <#if review.data?is_date>
                                ${review.data?date?string("dd/MM/yyyy")}
                            <#else>
                                ${review.data!""}
                            </#if>
                            ${review.orario!""}
                        </span>
                    </div>
                </#list>
            </div>
        </section>

        <!-- Contacts Section -->
        <section id="contatti" class="contacts">
            <h2>Contatti</h2>
            <ul>
                <li><i class="fas fa-phone"></i> 06 1234 5678</li>
                <li><i class="fas fa-mobile-alt"></i> 345 678 9012</li>
                <li><i class="fas fa-envelope"></i> info@homerestaurant.it</li>
                <li><i class="fas fa-envelope"></i> commerciale@homerestaurant.it</li>
                <li><i class="fas fa-calendar-alt"></i> Giorni e orari di chiusura</li>
            </ul>
            <div class="orari-apertura">
                <h3><i class="fas fa-door-open"></i> Orari di Apertura</h3>
                <table>
                    <tr>
                        <td>Lunedì</td>
                        <td><strong>Chiuso</strong></td>
                    </tr>
                    <tr>
                        <td>Martedì - Giovedì</td>
                        <td>12:00 - 15:00 | 19:00 - 23:00</td>
                    </tr>
                    <tr>
                        <td>Venerdì - Sabato</td>
                        <td>12:00 - 15:00 | 19:00 - 00:00</td>
                    </tr>
                    <tr>
                        <td>Domenica</td>
                        <td>12:00 - 15:00 | 19:00 - 22:30</td>
                    </tr>
                </table>
                <p class="note">* Apertura straordinaria su prenotazione per gruppi.</p>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl" />

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>