<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Calendario | Home Restaurant</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/admin_calendar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl" />

    <main class="admin-container">
        <div class="calendar-header">
            <a href="${contextPath}/Proprietario/showPanel" class="back-arrow">
                <i class="fa-solid fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-calendar-alt"></i> Gestione Calendario</h1>
        </div>

        <section class="calendar-section">
           
            <h2><i class="fas fa-clock"></i> Orari Settimanali</h2>

            <table class="calendar-table">
                <thead>
                    <tr>
                        <th>Giorno</th>
                        <th>Apertura</th>
                        <th>Chiusura</th>
                        <th>Stato</th>
                        <th>Stato</th>
                    </tr>
                </thead>
                <tbody>
                    <#list giorniChiusuraSettimanali as day>
                        <tr>
                            <form method="post" action="${contextPath}/Proprietario/editDay">
                                <td>${day.data}</td>
                                <td>
                                    <input type="time" name="orariapertura" value="${day.orarioApertura!}"/>

                                </td>
                                <td>
                                    <input type="time" name="orarichiusura" value="${day.orarioChiusura!}"/>
                                  
                                </td>
                                <td>
                                    <select name="orari[stato]">
                                        <option value="aperto" <#if day.aperto?? && day.aperto == true>selected</#if>>Aperto</option>
                                        <option value="chiuso" <#if day.aperto?? && day.aperto == false>selected</#if>>Chiuso</option>
                                    </select>
                                </td>
                                <td>
                                    <input type="hidden" name="giorno" value="${day.data}">
                                    <button type="submit" class="btn-save">Aggiorna</button>
                                </td>
                            </form>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </section>

        <!-- Chiusure Straordinarie -->
        <section class="calendar-section">
            <h2><i class="fas fa-ban"></i> Chiusure Straordinarie</h2>

            <!-- Aggiunta -->
            <form method="post" action="${contextPath}/Proprietario/addExceptionDay" class="add-date-form">
                <label for="dataChiusura"><i class="fas fa-plus-circle"></i> Aggiungi data di chiusura:</label>
                <input type="date" name="dataChiusura" required>
                <button type="submit" class="btn-add">Aggiungi</button>
            </form>

            <!-- Elenco -->
            <#if giorniChiusuraEccezionali?size gt 0>
                <table class="calendar-table">
                    <thead>
                        <tr>
                            <th>Data</th>
                            <th>Azione</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list giorniChiusuraEccezionali as eccezione>
                            <tr>
                                <td>${eccezione.exceptionDate}</td>
                                <td>
                                    <form method="post" action="${contextPath}/Proprietario/deleteExceptionDay" class="remove-date-form">
                                        <input type="hidden" name="dataChiusura" value="${eccezione.exceptionDateRaw}">
                                        <button type="submit" class="btn-remove"><i class="fas fa-trash-alt"></i> Rimuovi</button>
                                    </form>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <p class="no-entries"><i class="fas fa-info-circle"></i> Nessuna chiusura straordinaria impostata.</p>
            </#if>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl" />

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>
