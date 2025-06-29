<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Scrivi una Recensione</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/form.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/recensioni.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

    <#include "header.ftl">

    <main>
        <section class="form-section">
            <div class="form-container">
                <h1>Scrivi una Recensione</h1>

                <form method="POST" action="/DeliveryHomeRestaurant/Recensione/writeReview/" class="form">

                    <!-- Descrizione recensione -->
                    <div class="form-group">
                        <label for="description">Descrizione</label>
                        <textarea id="description" name="description" rows="5" maxlength="1000" placeholder="Scrivi qui la tua recensione..." required></textarea>
                    </div>

                    <!-- Valutazione a stelle -->
                    <div class="form-group">
                        <label for="vote">Valutazione</label>
                        <div class="star-rating">
                            <input type="radio" name="vote" id="star5" value="5"><label for="star5">&#9733;</label>
                            <input type="radio" name="vote" id="star4" value="4"><label for="star4">&#9733;</label>
                            <input type="radio" name="vote" id="star3" value="3"><label for="star3">&#9733;</label>
                            <input type="radio" name="vote" id="star2" value="2"><label for="star2">&#9733;</label>
                            <input type="radio" name="vote" id="star1" value="1"><label for="star1">&#9733;</label>
                        </div>
                    </div>

                    <button type="submit">Invia Recensione</button>

                </form>
            </div>
        </section>
    </main>

    <#include "footer.ftl">

</body>
</html>

