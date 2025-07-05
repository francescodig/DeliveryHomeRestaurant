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
    <#include "header.ftl">

    <main>
        <section class="hero">
            <div class="hero-content">
                <h1>HOME RESTAURANT</h1>
                <h2>HOME DELIVERY SERVICE</h2>
            </div>
        </section>

        <section class="account-container">
            <h2>Gestione Account</h2>

            <div class="password-section">
                <h3>Modifica Profilo</h3>
                <form action="/DeliveryHomeRestaurant/User/modifyProfile" method="POST">
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

            <div class="password-section">
                <h3>Cambia Password</h3>
                <form action="/DeliveryHomeRestaurant/User/changePassword" method="POST">
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

            <#if role?? && role == "cliente">
            <div class="address-section">
                <h3>I miei indirizzi</h3>
                <#if indirizzi?has_content>
                    <ul class="address-list">
                        <#list indirizzi as indirizzo>
                            <#if indirizzo.attivo>
                                <li class="address-item">
                                    <span>
                                        <i class="fas fa-map-marker-alt"></i>
                                    
                                            ${indirizzo.via}, ${indirizzo.civico}, ${indirizzo.cap} ${indirizzo.citta}
                                    
                                    </span>
                                    <form action="/DeliveryHomeRestaurant/User/removeAddress" method="POST" class="remove-address-form">
                                        <input type="hidden" name="indirizzo_id" value="${indirizzo.id}">
                                        <button type="submit" class="remove-address-btn" title="Rimuovi Indirizzo">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </form>
                                </li>
                            </#if>
                        </#list>
                    </ul>
                <#else>
                    <p>Nessun indirizzo registrato.</p>
                </#if>
                <button type="button" data-modal-target="addressModal" class="btn-link-modal">
                    <i class="fas fa-plus"></i> Aggiungi indirizzo
                </button>
            </div>

            <div class="credit-cards-section">
                <h3>Le mie carte di credito</h3>
                <#if carte_credito?has_content>
                    <ul class="cards-list">
                        <#list carte_credito as carta>
                            <#if carta.attivo>
                                <li class="card-item">
                                    <span>
                                        <i class="far fa-credit-card"></i>
                                        ${carta.nomeIntestatario}
                                    </span>
                                    <form action="/DeliveryHomeRestaurant/User/removeCreditCard" method="POST" class="remove-card-form">
                                        <input type="hidden" name="numero_carta" value="${carta.numeroCarta}">
                                        <button type="submit" class="remove-card-btn" title="Rimuovi Metodo di Pagamento">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </form>
                                </li>
                            </#if>
                        </#list>
                    </ul>
                <#else>
                    <p>Nessuna carta di credito registrata.</p>
                </#if>
                <button type="button" data-modal-target="cardModal" class="btn-link-modal">
                    <i class="fas fa-plus"></i> Aggiungi carta
                </button>
            </div>
            <#else>
            </#if>
            
            <#if role == "cliente">
                <div class="orders-link">
                    <a href="/DeliveryHomeRestaurant/User/showMyOrders/" class="btn-link">
                        <i class="fas fa-box-open"></i> I miei ordini
                    </a>
                </div>
            </#if>

            <div class="logout-section">
                <a href="/DeliveryHomeRestaurant/User/logoutUser/" class="btn-logout">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
        </section>
    </main>

    <#include "footer.ftl">

    <!-- Modal Aggiungi Indirizzo -->
    <div id="addressModal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>Aggiungi un nuovo indirizzo</h2>
            <form method="POST" action="/DeliveryHomeRestaurant/User/addAddress" class="form">
                <div class="form-group">
                    <label for="via">Via</label>
                    <input type="text" id="via" name="via" required>
                </div>
                <div class="form-group">
                    <label for="civico">Civico</label>
                    <input type="text" id="civico" name="civico" pattern="^[0-9]+[a-zA-Z]?$" required>
                </div>
                <div class="form-group">
                    <label for="citta">Città</label>
                    <select id="citta" name="citta" required>
                        <option value="">-- Seleziona una città --</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="cap">CAP</label>
                    <input type="text" id="cap" name="cap" pattern="\d{5}" inputmode="numeric" maxlength="5" title="Inserisci un CAP valido (5 cifre)" readonly required>
                </div>
                <button type="submit">Salva Indirizzo</button>
            </form>
        </div>
    </div>

    <!-- Modal Aggiungi Carta -->
    <div id="cardModal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <h2>Aggiungi un nuovo metodo di pagamento</h2>
            <form method="POST" action="/DeliveryHomeRestaurant/User/addCreditCard" class="form">
                <div class="form-group">
                    <label for="nome_carta">Nominativo Carta</label>
                    <input type="text" id="nome_carta" name="nome_carta" required>
                </div>
                <div class="form-group">
                    <label for="numero_carta">Numero Carta</label>
                    <input type="text" id="numero_carta" name="numero_carta" pattern="^\d{16}$" inputmode="numeric" maxlength="16" title="Inserisci 16 cifre numeriche" required>
                </div>
                <div class="form-group">
                    <label for="cvv">CVV</label>
                    <input type="text" id="cvv" name="cvv" pattern="^\d{3,4}$" inputmode="numeric" maxlength="4" title="Inserisci un CVV di 3 o 4 cifre" required>
                </div>
                <div class="form-group">
                    <label for="nome_intestatario">Nome Intestatario</label>
                    <input type="text" id="nome_intestatario" name="nome_intestatario" pattern="^[a-zA-ZÀ-ÿ' ]{2,50}$" title="Inserisci un nome valido (solo lettere e spazi)" required>
                </div>
                <div class="form-group">
                    <label for="data_scadenza">Data Scadenza</label>
                    <input type="text" id="data_scadenza" name="data_scadenza" pattern="^(0[1-9]|1[0-2])/\d{2}$" inputmode="numeric" placeholder="MM/AA" required>
                </div>
                <button type="submit">Salva Metodo di Pagamento</button>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const select = document.getElementById('citta');
            const capInput = document.getElementById('cap');

            fetch('${contextPath}/resources/comuni_aq.json')
                .then(response => response.json())
                .then(comuni => {
                    comuni.forEach(comune => {
                        const option = document.createElement('option');
                        option.value = comune.nome;
                        option.textContent = comune.nome;
                        option.dataset.cap = comune.cap[0];
                        select.appendChild(option);
                    });
                })
                .catch(error => {
                    console.error('Errore nel caricamento dei comuni:', error);
                });

            select.addEventListener('change', function () {
                const selectedOption = this.options[this.selectedIndex];
                const cap = selectedOption.dataset.cap;
                if (cap) {
                    capInput.value = cap;
                }
            });
        });
    </script>

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>
    <script src="${contextPath}/resources/Js/modal.js" defer></script>
</body>
</html>
