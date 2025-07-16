<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Crea Account Collaboratore | Admin Panel</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css" />
    <link rel="stylesheet" href="${contextPath}/resources/css/create_account_admin.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
</head>
<body>
    <!-- Header -->
    <#include "header.ftl" />

    <!-- Main Content -->
    <main class="admin-container">
        <div class="admin-header">
            <a href="${contextPath}/Proprietario/showPanel" class="back-button">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-user-plus"></i> Crea Nuovo Account</h1>
            <p class="admin-subtitle">Aggiungi un nuovo collaboratore al tuo team</p>
        </div>

        <section class="form-container">
            <!-- Error Section -->
            <#include "error_section.ftl">

            <div class="form-card">
                <form action="${contextPath}/Proprietario/createEmployee" method="POST">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="nome">
                                <i class="fas fa-user"></i> Nome
                            </label>
                            <input type="text" id="nome" name="nome" placeholder="Mario" required />
                        </div>

                        <div class="form-group">
                            <label for="cognome">
                                <i class="fas fa-user"></i> Cognome
                            </label>
                            <input type="text" id="cognome" name="cognome" placeholder="Rossi" required />
                        </div>

                        <div class="form-group">
                            <label for="email">
                                <i class="fas fa-envelope"></i> Email
                            </label>
                            <input type="email" id="email" name="email" placeholder="mario.rossi@example.com" required />
                        </div>

                        <div class="form-group">
                            <label for="password">
                                <i class="fas fa-lock"></i> Password
                            </label>
                            <div class="password-input">
                                <input type="password" id="password" name="password" required />
                                <button type="button" class="toggle-password">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="ruolo">
                                <i class="fas fa-user-tag"></i> Ruolo
                            </label>
                            <select id="ruolo" name="ruolo" required>
                                <option value="">-- Seleziona un ruolo --</option>
                                <option value="Cuoco">Cuoco</option>
                                <option value="Rider">Rider</option>
                            </select>
                        </div>

                        <div class="form-group full-width">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-user-plus"></i> Crea Account
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </section>

        <!-- Lista Collaboratori -->
        <section class="collaborators-list">
            <!-- Sezione Chef -->
            <div class="role-section">
                <h2><i class="fas fa-utensils"></i> Chef</h2>
                <#if chefs?has_content>
                    <table class="collaborators-table">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Email</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list chefs as chef>
                                <tr data-id="${chef.id}">
                                    <td>${chef.nome} ${chef.cognome}</td>
                                    <td>${chef.email}</td>
                                    <td class="actions">
                                        <form action="${contextPath}/Proprietario/deleteEmployee" method="POST" onsubmit="return confirm('Sei sicuro di voler eliminare questo collaboratore?');" class="inline-form">
                                            <input type="hidden" name="employeeId" value="${chef.id}" />
                                            <button type="submit" class="btn btn-delete">
                                                <i class="fas fa-trash-alt"></i> Elimina
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <div class="no-collaborators">
                        <i class="fas fa-info-circle"></i>
                        <p>Nessuno chef registrato</p>
                    </div>
                </#if>
            </div>

            <!-- Sezione Rider -->
            <div class="role-section">
                <h2><i class="fas fa-motorcycle"></i> Rider</h2>
                <#if riders?has_content>
                    <table class="collaborators-table">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Email</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list riders as rider>
                                <tr data-id="${rider.id}">
                                    <td>${rider.nome} ${rider.cognome}</td>
                                    <td>${rider.email}</td>
                                    <td class="actions">
                                        <form action="${contextPath}/Proprietario/deleteEmployee" method="POST" class="inline-form">
                                            <input type="hidden" name="employeeId" value="${rider.id}" />
                                            <button type="submit" class="btn btn-delete">
                                                <i class="fas fa-trash-alt"></i> Elimina
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                <#else>
                    <div class="no-collaborators">
                        <i class="fas fa-info-circle"></i>
                        <p>Nessun rider registrato</p>
                    </div>
                </#if>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <#include "footer.ftl" />

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
</body>
</html>
