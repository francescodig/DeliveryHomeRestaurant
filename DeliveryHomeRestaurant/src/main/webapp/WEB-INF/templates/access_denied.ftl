<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Accesso Negato</title>
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
        <i class="fas fa-utensils fork"></i>
      </div>

      <h1>Accesso Negato</h1>
      <p>Non hai i permessi necessari per accedere a questa sezione.</p>
      <a class="button" href="${contextPath}/DeliveryHomeRestaurant/User/home">Torna alla Homepage</a>
    </section>
  </main>

  <!-- Footer -->
  <#include "footer.ftl">

  <script src="${contextPath}/resources/Js/hamburger.js"></script>
  <script src="${contextPath}/resources/Js/theme.js" defer></script>

</body>
</html>
