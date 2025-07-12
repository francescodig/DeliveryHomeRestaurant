<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Errore</title>
  <link rel="icon" type="image/x-icon" href="${contextPath}/Smarty/Immagini/favicon.ico">
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
        <i class="fas fa-exclamation-circle fork"></i>
      </div>

      <h1>Si è verificato un errore</h1>
      <p>Ci dispiace, qualcosa è andato storto durante l'elaborazione della tua richiesta.</p>
      
      <#if statusCode??>
        <p><strong>Codice errore:</strong> ${statusCode}</p>
      </#if>

      <#if exception??>
        <p><strong>Dettagli:</strong> ${exception?string}</p>
      </#if>

      <a class="button" href="${contextPath}/Rider/showOrders">Torna alla schermata ordini</a>
    </section>
  </main>

  <!-- Footer -->
  <#include "footer.ftl">

  <script src="${contextPath}/resources/Js/hamburger.js"></script>
  <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>

