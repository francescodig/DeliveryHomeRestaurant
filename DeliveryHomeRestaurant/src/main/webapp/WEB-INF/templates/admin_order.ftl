<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Gestione Ordini | Home Restaurant</title>
    <link rel="stylesheet" href="/resources/css/layout.css">
    <link rel="stylesheet" href="/resources/css/admin_orders.css">
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
            <h1><i class="fas fa-shopping-bag"></i> Gestione Ordini</h1>
            <p class="admin-subtitle">Visualizza e gestisci tutti gli ordini dei clienti</p>
        </div>
        
        <!-- Filtri e Ricerca -->
        <section class="filters-section">
            <div class="filters-grid">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchOrders" placeholder="Cerca ordini...">
                </div>
                <div class="filter-group">
                    <label for="filterStatus"><i class="fas fa-filter"></i> Filtra per stato:</label>
                    <select id="filterStatus">
                        <option value="all">Tutti gli stati</option>
                        <option value="in_attesa">In attesa</option>
                        <option value="in_preparazione">In preparazione</option>
                        <option value="pronto">Pronto</option>
                        <option value="consegnato">Consegnato</option>
                        <option value="annullato">Annullato</option>
                    </select>
                </div>
                <div class="filter-group">
                    <label for="filterDate"><i class="far fa-calendar-alt"></i> Ordina per:</label>
                    <select id="filterDate">
                        <option value="newest">Più recenti</option>
                        <option value="oldest">Più vecchi</option>
                    </select>
                </div>
            </div>
        </section>

        <!-- Lista Ordini -->
        <section class="orders-list">
            <div class="orders-header">
                <h2><i class="fas fa-list"></i> Tutti gli Ordini</h2>
            </div>
            
            <#if orders?size > 0>
                <table class="orders-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Data</th>
                            <th>Totale</th>
                            <th>Stato</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list orders as order>
                            <tr data-id="${order.id}">
                                <td>#${order.id}</td>
                                <td>${order.cliente.nome} ${order.cliente.cognome}</td>
                                <td>${order.dataEsecuzione?string("dd/MM/yyyy HH:mm")}</td>
                                <td>€${order.costo}</td>
                                <td>
                                    <span class="status-badge ${order.stato}">
                                        ${order.stato?capitalize}
                                    </span>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <div class="no-orders">
                    <i class="far fa-frown"></i>
                    <p>Nessun ordine trovato</p>
                </div>
            </#if>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="/resources/js/hamburger.js"></script>
    <script src="/resources/js/theme.js"></script>
    <script src="/resources/js/admin_orders.js"></script>
</body>
</html>
