<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Recensioni | Home Restaurant</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/recensioni.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main class="admin-container">
        <div class="admin-header">
            <a href="/Delivery/Proprietario/showPanel" class="back-button">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-star"></i> Gestione Recensioni</h1>
            <p class="admin-subtitle">Visualizza e gestisci tutte le recensioni dei clienti</p>
        </div>
        
        <!-- Filtri e Ricerca -->
        <section class="filters-section">
            <div class="filters-grid">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchReviews" placeholder="Cerca recensioni...">
                </div>
                <div class="filter-group">
                    <label for="filterRating"><i class="fas fa-filter"></i> Filtra per voto:</label>
                    <select id="filterRating">
                        <option value="all">Tutti i voti</option>
                        <option value="5">5 stelle</option>
                        <option value="4">4 stelle</option>
                        <option value="3">3 stelle</option>
                        <option value="2">2 stelle</option>
                        <option value="1">1 stella</option>
                    </select>
                </div>
                <div class="filter-group">
                    <label for="filterDate"><i class="far fa-calendar-alt"></i> Ordina per:</label>
                    <select id="filterDate">
                        <option value="newest">Più recenti</option>
                        <option value="oldest">Più vecchie</option>
                    </select>
                </div>
            </div>
        </section><br>

        <!-- Lista Recensioni -->
        <section class="reviews-list">
            <div class="reviews-header">
                <h2><i class="fas fa-list"></i> Tutte le Recensioni</h2>
            </div>
            
            <#if reviews?size > 0>
                <div class="reviews-grid">
                    <#list reviews as review>
                        <div class="review-card">
                            <div class="review-header">
                                <div class="user-info">
                                    <div class="user-avatar">
                                        <i class="fas fa-user-circle"></i>
                                    </div>
                                    <div class="user-details">
                                        <h3>${review.cliente.nome?html} ${review.cliente.cognome?html}</h3>
                                        <span class="review-date">${review.data?string("dd/MM/yyyy HH:mm")}</span>
                                    </div>
                                </div>
                                <div class="review-rating">
                                    <#list 1..5 as i>
                                        <#if i <= review.voto>
                                            <i class="fas fa-star filled"></i>
                                        <#else>
                                            <i class="far fa-star"></i>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                            <div class="review-content">
                                <p>${review.descrizione?html}</p>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="no-reviews">
                    <i class="far fa-frown"></i>
                    <p>Nessuna recensione trovata</p>
                </div>
            </#if>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>

