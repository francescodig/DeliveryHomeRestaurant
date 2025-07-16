<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Prezzo Modificato</title>
  <link rel="icon" type="image/x-icon" href="${contextPath}/resources/Immagini/favicon.ico">
  <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
  <link rel="stylesheet" href="${contextPath}/resources/css/error.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

  <!-- Header -->
  <#include "header.ftl">

  <!-- Main Content -->
  <main>
    <section class="accesso-negato-container">
      <div class="crossed-utensils">
        <i class="fas fa-exclamation-triangle fork"></i>
      </div>

      <h1>Attenzione</h1>
      <p>Uno o più prodotti nel tuo carrello hanno subito una variazione di prezzo.</p>
      <p>Per favore, controlla i dettagli aggiornati prima di procedere con l'ordine.</p>
      <a class="button" href="${contextPath}/User/order">Torna all'Ordine</a>
    </section>
  </main>

  <!-- Footer -->
  <#include "footer.ftl">

  <script src="${contextPath}/resources/Js/hamburger.js"></script>
  <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
