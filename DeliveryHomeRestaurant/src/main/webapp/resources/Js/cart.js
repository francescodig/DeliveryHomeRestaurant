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

        item.querySelector('.add-button').addEventListener('click', () => {
            openModalWithProduct(name, price);
        });
    });

    function addToCart(name, price) {
        const existing = cart.find(item => item.name === name);
        if (existing) {
            existing.qty += 1;
        } else {
            cart.push({ name, price, qty: 1 });
        }
        renderCart();
        showCartIcon();
        localStorage.setItem('cart', JSON.stringify(cart));
    }

    function showCartIcon() {
        if (cart.length > 0) {
            cartIcon.classList.remove('hidden');
            const totalQty = cart.reduce((sum, item) => sum + item.qty, 0);
            cartBadge.textContent = totalQty;
        }
    }

    function renderCart() {
        cartItems.innerHTML = '';
        let total = 0;
        cart.forEach(item => {
            const li = document.createElement('li');
            li.textContent = `${item.name} x${item.qty} - €${(item.price * item.qty).toFixed(2)}`;
            cartItems.appendChild(li);
            total += item.price * item.qty;
        });
        cartTotal.textContent = `Totale: €${total.toFixed(2)}`;
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

// --- Gestione modale ---
    const modal = document.getElementById('product-modal');
    const modalBody = document.getElementById('modal-body');
    const closeBtn = document.querySelector('.close-button');

    function openModalWithProduct(name, price) {
        modal.classList.remove('hidden');
        modalBody.innerHTML = `
            <h2>${name}</h2>
            <p>Prezzo: €${price.toFixed(2)}</p>
            <p>Qui puoi aggiungere descrizione, opzioni extra, immagini, ecc.</p>
            <button onclick="confirmAddToCart('${name}', ${price})">Aggiungi al carrello</button>
        `;
    }

    function confirmAddToCart(name, price) {
        addToCart(name, price);
        modal.classList.add('hidden');
    }

    closeBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
    });
