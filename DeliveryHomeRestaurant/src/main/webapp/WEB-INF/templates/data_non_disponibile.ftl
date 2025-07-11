<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Data non disponibile</title>
  <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
  <link rel="stylesheet" href="${contextPath}/resources/css/error.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

  <!-- Header -->
  <#include "header.ftl" />

  <!-- Main Content -->
  <main>
    <section class="accesso-negato-container">
      <div class="crossed-utensils">
        <i class="fas fa-calendar-times fork"></i>
      </div>

      <h1>Data non disponibile</h1>
      <p>La data o l'orario da te selezionati non sono disponibili perché il ristorante è chiuso.</p>
      <p>Ti invitiamo a scegliere un'altra data o orario tra quelli disponibili.</p>
      <a class="button" href="/Delivery/User/order/">Torna alla pagina Ordina</a>
    </section>
  </main>

  <!-- Footer -->
  <#include "footer.ftl" />

  <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
