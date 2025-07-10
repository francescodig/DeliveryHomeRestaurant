/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */



document.addEventListener("DOMContentLoaded", function () {
    // Setup carrello
    let cart = localStorage.getItem("cart") ? JSON.parse(localStorage.getItem("cart")) : [];
    const cartForm = document.getElementById("cartForm");

    cartForm.addEventListener("submit", function () {
        const currentCart = localStorage.getItem("cart");
        document.getElementById("cartDataInput").value = currentCart;
    });

    renderCart();
    showCartIcon();


    // Ultimo menu salvato localmente per evitare aggiornamenti inutili
    let lastMenuJSON = null;

    function aggiornaMenu() {
        fetch("/DeliveryHomeRestaurant/User/getMenuJson")
            .then(res => res.json())
            .then(menu => {
                const currentMenuJSON = JSON.stringify(menu);
                if (currentMenuJSON !== lastMenuJSON) {
                    renderMenu(menu);
                    lastMenuJSON = currentMenuJSON;
                }
            })
            .catch(err => console.error("Errore aggiornamento menù:", err));
    }



    function renderMenu(menu) {
    const container = document.querySelector(".menu-section");
    if (!container) return;

    let html = '<h1>Ordina dal menù</h1>';

    menu.forEach(categoria => {
        let icona = "";
        switch (categoria.categoria) {
            case "Antipasti": icona = '<i class="fa-solid fa-martini-glass-citrus"></i>'; break;
            case "Primi": icona = '<i class="fas fa-bread-slice"></i>'; break;
            case "Secondi": icona = '<i class="fas fa-pizza-slice"></i>'; break;
            case "Dolci": icona = '<i class="fa-solid fa-ice-cream"></i>'; break;
            case "Bevande": icona = '<i class="fas fa-glass-whiskey"></i>'; break;
        }

        html += `<div class="menu-category">
                    <h2>${icona} ${categoria.categoria}</h2>
                    <div class="menu-items">`;

        categoria.piatti.forEach(piatto => {
            html += `
                <div class="menu-item">
                    <div class="item-info">
                        <h3>${piatto.nome}</h3>
                        <p>${piatto.descrizione}</p>
                    </div>
                    <div class="item-price">€${piatto.costo}</div>
                    <button class="add-button" data-id="${piatto.id}" 
                            data-name="${piatto.nome}" 
                            data-price="${piatto.costo}" 
                            data-descrizione="${piatto.descrizione || ''}">
                            +
                    </button>
                </div>`;
        });

        html += `</div></div>`;
    });

    container.innerHTML = html;

    //  Aggiungi listener ai bottoni "+" appena generati dopo aver aggiornato il menù
    document.querySelectorAll('.add-button').forEach(button => {
        button.addEventListener('click', () => {
            const id = button.dataset.id;
            const name = button.dataset.name;
            const price = parseFloat(button.dataset.price.replace(',', '.'));
            const descrizione = button.dataset.descrizione;
            openModalWithProduct(id, name, price, descrizione);
        });
    });
}

    // Primo aggiornamento subito
    aggiornaMenu();

    // Poi ogni 60 secondi
    setInterval(aggiornaMenu, 60000);
});
