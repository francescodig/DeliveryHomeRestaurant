<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Segnalazioni | Home Restaurant</title>
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
            <a href="${contextPath}/Proprietario/showPanel" class="back-button">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-star"></i> Gestione Segnalazioni</h1>
            <p class="admin-subtitle">Visualizza tutte le segnalazioni dei clienti</p>
        </div>

        <!-- Filtri e Ricerca -->

        <!-- Error Section -->
        <#include "error_section.ftl">

            <section class="filters-section">
            <form method="get" action="${contextPath}/Proprietario/showSegnalazioni/" id="filterForm">
                <div class="filters-grid">
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text" name="search" value="${search?if_exists?html}" placeholder="Cerca recensioni...">
                    </div>
                    <div class="filter-group">
                        <label for="sort"><i class="far fa-calendar-alt"></i> Ordina per:</label>
                        <select name="sort" id="sort" onchange="document.getElementById('filterForm').submit()">
                            <option value="newest" <#if sort == "newest">selected</#if>>Più recenti</option>
                            <option value="oldest" <#if sort == "oldest">selected</#if>>Più vecchie</option>
                        </select>
                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i></button>
                    </div>
                </div>
            </form>
            </section><br>

        <!-- Lista Segnalazioni -->
        <section class="reviews-list">
            <div class="reviews-header">
                <h2><i class="fas fa-list"></i> Tutte le Segnalazioni</h2>
            </div>

            <#if segnalazioni?has_content>
                <div class="reviews-grid">
                    <#list segnalazioni as segnalazione>
                        <div class="review-card">
                            <div class="review-header">
                                <div class="user-info">
                                    <div class="user-avatar">
                                        <i class="fas fa-user-circle"></i>
                                    </div>
                                    <div class="user-details">
                                        <h3>
                                            ${segnalazione.ordine.cliente.nome} ${segnalazione.ordine.cliente.cognome}
                                        </h3>
                                        <p>${segnalazione.getCliente().getEmail()}</p>
                                        <span class="review-date">
                                          <#-- converto la stringa in data -->
                                            <#assign dataObj = segnalazione.data?datetime("yyyy-MM-dd'T'HH:mm")>
                                            ${dataObj?string("dd/MM/yyyy HH:mm")}
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="review-content">
                                <p>${segnalazione.descrizione}</p>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="no-reviews">
                    <i class="far fa-frown"></i>
                    <p>Nessuna segnalazione trovata</p>
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

