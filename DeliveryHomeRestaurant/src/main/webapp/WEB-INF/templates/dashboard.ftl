<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Admin Dashboard - Home Restaurant</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css" />
    <link rel="stylesheet" href="${contextPath}/resources/css/dashboard.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <!-- Header -->
    <#include "header.ftl" />

    <!-- Main Content -->
    <div class="dashboard-header">
        <a href="${contextPath}/Proprietario/showPanel" class="back-button">
            <i class="fas fa-arrow-left"></i>
        </a>
        <h1><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
    </div>

    <!-- Statistiche Rapide -->
    <section class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon">
                <i class="fas fa-euro-sign"></i>
            </div>
            <div class="stat-info">
                <h3>Fatturato Oggi</h3>
                <p>€${totaleOggi?string["#,##0.00"]}</p>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon">
                <i class="fas fa-shopping-bag"></i>
            </div>
            <div class="stat-info">
                <h3>Ordini Oggi</h3>
                <p>${ordiniOggi}</p>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon">
                <i class="fas fa-users"></i>
            </div>
            <div class="stat-info">
                <h3>Nuovi Clienti</h3>
                <p>${numeroClienti}</p>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-icon">
                <i class="fas fa-star"></i>
            </div>
            <div class="stat-info">
                <h3>Valutazione Media</h3>
                <p>${mediaValutazioni?string["0.0"]}/5</p>
            </div>
        </div>
    </section>

    <!-- Grafici -->
    <section class="charts-section">
        <div class="chart-container">
            <h2><i class="fas fa-chart-line"></i> Fatturato Ultimi 7 Giorni</h2>
            <canvas id="revenueChart"></canvas>
        </div>
        <div class="chart-container">
            <h2><i class="fas fa-pizza-slice"></i> Piatti Più Venduti</h2>
            <canvas id="dishesChart"></canvas>
        </div>
    </section>

    <!-- Ultimi Ordini - SEZIONE DINAMICA -->
    <section class="recent-orders">
        <h2><i class="fas fa-history"></i> Ultimi Ordini</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cliente</th>
                    <th>Importo</th>
                    <th>Stato</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
                <#list orders as ordine>
                    <tr>
                        <td>#${ordine.getId()}</td>
                        <td>${ordine.getCliente().getNome()} ${ordine.getCliente().getCognome()}</td>
                        <td>€${ordine.getCosto()?string["#,##0.00"]}</td>
                        <td>
                            <span class="status ${ordine.getStato()?lower_case}">
                                ${ordine.getStato()?cap_first}
                            </span>
                        </td>
                        <td><a href="/ordini/dettagli/${ordine.getId()}" class="btn-details">Dettagli</a></td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </section>

    <!-- Footer -->
    <#include "footer.ftl" />

    <!-- Script per i grafici -->
    <script>
        // Grafico Fatturato
        const revenueCtx = document.getElementById('revenueChart').getContext('2d');
        new Chart(revenueCtx, {
            type: 'bar',
            data: {
                labels: ['Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab', 'Dom'],
                datasets: [{
                    label: 'Fatturato (€)',
                    data: [<#list fatturatoSettimana as valore>${valore}<#if valore?has_next>, </#if></#list>],
                    backgroundColor: '#046C6D',
                    borderColor: '#035050',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });

        // Grafico Piatti Più Venduti
        const dishesCtx = document.getElementById('dishesChart').getContext('2d');
        new Chart(dishesCtx, {
            type: 'doughnut',
            data: {
                labels: [<#list nomiTopPiatti as nome>"${nome}"<#if nome?has_next>, </#if></#list>],
                datasets: [{
                    data: [<#list quantitaTopPiatti as qta>${qta}<#if qta?has_next>, </#if></#list>],
                    backgroundColor: ['#046C6D', '#035050', '#03A6A6', '#04B2B2', '#05C0C0', '#027373', '#059A9A', '#02B8B8', '#04CCCC', '#01A1A1']
                }]
            },
            options: {
                responsive: true
            }
        });
    </script>

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>

