//gestione del carrello e apertura pagina modale di un prodotto

// --- Gestione carrello ---
    const cartIcon = document.getElementById('cart-icon');
    const cartBadge = document.getElementById('cart-badge');
    let cart = [];
    const cartItems = document.getElementById('cart-items');
    const cartTotal = document.getElementById('cart-total');
    const cartElement = document.getElementById('cart');

    // Aggiungi event listener ai bottoni "+"
    document.querySelectorAll('.menu-item').forEach(item => {
        const name = item.querySelector('h3').textContent;
        const price = parseFloat(item.querySelector('.item-price').textContent.replace('€', '').replace(',', '.'));
        const id = item.querySelector('.add-button').dataset.id;
        const descrizioneElement = item.querySelector('.item-info p');
        const descrizione = descrizioneElement ? descrizioneElement.textContent.trim() : '';

        item.querySelector('.add-button').addEventListener('click', () => {
            openModalWithProduct(id, name, price, descrizione);
        });
    });

    function addToCart(id, name, price) {
        const existing = cart.find(item => item.id === id);
        if (existing) {
            existing.qty += 1;
        } else {
            cart.push({id, name, price, qty: 1 });
            localStorage.setItem("cart_createdAt", Date.now());
        }
        renderCart();
        showCartIcon();
        localStorage.setItem('cart', JSON.stringify(cart));
    }

    function removeFromCart(id) {
        cart = cart.filter(item => item.id !== id);
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCart();
        showCartIcon();
    }

    function showCartIcon() {
        if (cart.length > 0) {
            cartIcon.classList.remove('hidden');
            const totalQty = cart.reduce((sum, item) => sum + item.qty, 0);
            cartBadge.textContent = totalQty;
        } else {
            const totalQty = 0;
            cartIcon.classList.add('hidden');
        }
    }

    function renderCart() {
        cartItems.innerHTML = '';
        let total = 0;
        cart.forEach(item => {
            const li = document.createElement('li');
            li.innerHTML = 
                `${item.name} x${item.qty} - €${(item.price * item.qty).toFixed(2)}
                <button class="remove-btn" data-id="${item.id}">-</button>
            `;
            cartItems.appendChild(li);
            total += item.price * item.qty;
        });
        cartTotal.textContent = `Totale: €${total.toFixed(2)}`;
        cartItems.querySelectorAll('.remove-btn').forEach(button => {
            button.addEventListener('click', (e) => {
                const id = button.dataset.id;
                removeFromCart(id);
            });
        });
    }

    function toggleCart() {
        cartElement.classList.toggle('open');
    }

    cartIcon.addEventListener('click', (e) => {
        e.stopPropagation();
        toggleCart();
    });

    document.addEventListener('click', (e) => {
        if (!cartElement.contains(e.target) && e.target !== cartIcon) {
            cartElement.classList.remove('open');
            document.body.style.overflow = '';
        }
    });

    function closeCart() {
        cartElement.classList.remove('open');
        document.body.style.overflow = '';
    }

    function sendOrderToServer() {
        fetch('/Delivery/Ordine/confirmOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                cart: cart,
                createdAt: localStorage.getItem("cart_createdAt")})
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Ordine confermato!');
                localStorage.removeItem('cart'); //Pulizia carrello in locale
                window.location.href = '/Delivery/Ordine/showConfirmOrder';
            } else {
                alert('Errore: ' + data.message);
            }
        })
        .catch(error => {
            alert('Errore nella richiesta:', error);
        });
    }

    function confirmOrder() {
        console.log("Funzione confirmOrder caricata correttamente.");
        
        const cart = localStorage.getItem("cart");
        const createdAt = localStorage.getItem("cart_createdAt");

        if (!cart) {
            document.getElementById("cart-items").innerHTML = "<li>Il carrello è vuoto.</li>";
            return;
        }

        try {
            const parsedCart = JSON.parse(cart);
            const createdDate = createdAt ? new Date(parseInt(createdAt)) : null;

            // Popola data
            if (createdDate) {
                document.getElementById("order-date").textContent = createdDate.toLocaleString();
            }

            // Popola prodotti
            const itemsContainer = document.getElementById("cart-items");
            let total = 0;
            itemsContainer.innerHTML = ""; // Pulisci

            parsedCart.forEach(item => {
                const li = document.createElement("li");
                li.textContent = `${item.name} x${item.qty} - €${(item.price * item.qty).toFixed(2)}`;
                itemsContainer.appendChild(li);
                total += item.price * item.qty;
            });
            document.getElementById("total-amount").textContent = total.toFixed(2);
        } catch (e) {
            console.error("Errore nel parsing del carrello:", e);
            document.getElementById("cart-items").innerHTML = "<li>Errore nel caricamento del carrello.</li>";
        }
    }

    function submitCart() {
        document.getElementById("cartDataInput").value = localStorage.getItem("cart");
        document.getElementById("createdAtInput").value = localStorage.getItem("cart_createdAt") || '';
        document.getElementById("cartForm").submit();
    }

// --- Gestione modale ---
    const modal = document.getElementById('product-modal');
    const modalBody = document.getElementById('modal-body');
    const closeBtn = document.querySelector('.close-button');

    function openModalWithProduct(id, name, price, descrizione) {
        modal.classList.remove('hidden');
        modalBody.innerHTML = `
            <h2>${name}</h2>
            <p>Prezzo: €${price.toFixed(2)}</p>
            <p>${descrizione || "Descrizione non disponibile"}</p>
            <button onclick="confirmAddToCart('${id}', '${name}', ${price})">Aggiungi al carrello</button>
        `;
    }

    function confirmAddToCart(id, name, price, descrizione) {
        addToCart(id, name, price, descrizione);
        modal.classList.add('hidden');
    }

    closeBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
    });
