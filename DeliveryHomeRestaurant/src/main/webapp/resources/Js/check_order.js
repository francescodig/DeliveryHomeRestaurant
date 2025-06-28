
function displayCartItems() {
    const cart = JSON.parse(localStorage.getItem("cart") || "[]");
    const cartItemsEl = document.getElementById("cart-items");
    const totalAmountEl = document.getElementById("total-amount");
    
    if (!cartItemsEl || !totalAmountEl) return;

    // Svuota la lista
    cartItemsEl.innerHTML = '';

    // Popola gli elementi del carrello
    cart.forEach(item => {
        const li = document.createElement('li');
        li.textContent = `${item.qty}x ${item.name} - €${(item.qty * item.price).toFixed(2)}`;
        cartItemsEl.appendChild(li);
    });

    // Calcola e mostra il totale
    const total = cart.reduce((sum, item) => sum + item.qty * item.price, 0);
    totalAmountEl.textContent = total.toFixed(2);
}
    

document.addEventListener('DOMContentLoaded', () => {
    displayCartItems();

    const validateDeliveryTime = () => {
        if (customTimeRadio.checked && customTimeInput.value) {
            const selectedTime = new Date(customTimeInput.value);
            const minTime = new Date(customTimeInput.min);
            
            if (selectedTime < minTime) {
                alert("L'orario di consegna non può essere antecedente all'orario proposto");
                return false;
            }
        }
        return true;
    }

    // Elementi del DOM
    const addressRadios = document.querySelectorAll('input[name="indirizzo_id"]');
    const timeLabel = document.getElementById('default-time-label');
    const timeInputHidden = document.getElementById('dataConsegna');
    const customTimeInput = document.getElementById('custom-delivery-time');
    const checkoutBtn = document.getElementById('checkout-button');
    const cartForm = document.getElementById('cartForm');
    const defaultTimeRadio = document.getElementById('default-time');
    const customTimeRadio = document.getElementById('custom-time');

    // Controllo elementi essenziali
    if (!addressRadios.length || !timeLabel || !timeInputHidden || !checkoutBtn || !cartForm) {
        console.error("Elementi mancanti nella pagina");
        return;
    }

    // 1. Gestione carrello
    const cart = JSON.parse(localStorage.getItem("cart") || "[]");
    const total = cart.reduce((sum, item) => sum + item.qty * item.price, 0);

    if (total === 0 || cart.length === 0) {
        checkoutBtn.disabled = true;
        checkoutBtn.classList.add("disabled");
        checkoutBtn.textContent = "Carrello vuoto";
        return; // Non procedere oltre se il carrello è vuoto
    }

    // 2. Funzione per aggiornare l'orario di consegna
    const aggiornaOrarioConsegna = (indirizzoId) => {
        fetch('/DeliveryHomeRestaurant/Ordine/getEstimatedTime', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                'cart_data': JSON.stringify(cart),
                'indirizzo_id': indirizzoId
            })
        })
        .then(response => {
            if (!response.ok) throw new Error("Errore nella richiesta");
            return response.json();
        })
        .then(data => {
            if (!data.estimated_time) throw new Error("Dati non validi dal server");

            const estimatedTime = new Date(data.estimated_time);
            const formattedTime = estimatedTime.toLocaleString('it-IT', {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                timeZone: 'Europe/Rome',
            });

            timeLabel.textContent = `Orario proposto: ${formattedTime}`;
            timeInputHidden.value = data.estimated_time;

            //Quello che segue è necesario per un problema di fusi orari (problema con toISOString)
            if (customTimeInput) {
                // Funzione di utilità per la conversione
                const toLocalDatetimeString = (date) => {
                    if (!date) return '';
                    
                    try {
                        const d = new Date(date);
                        // Compensa l'offset del fuso orario
                        d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
                        return d.toISOString().slice(0, 16);
                    } catch (e) {
                        console.error("Errore conversione data:", e);
                        return '';
                    }
                };

                    // Imposta il minimo (data stimata dal server)
                    customTimeInput.min = toLocalDatetimeString(data.estimated_time);

                    // Imposta valore di default (1 ora dopo la data stimata)
                    const defaultTime = new Date(data.estimated_time);
                    defaultTime.setHours(defaultTime.getHours() + 1);
                    customTimeInput.value = toLocalDatetimeString(defaultTime);
                    timeInputHidden.value = customTimeInput.value;
            }
        })
        .catch(err => {
            console.error("Errore durante il calcolo dell'orario:", err);
            timeLabel.textContent = "Impossibile calcolare l'orario di consegna.";
            timeInputHidden.value = "";
        });
    };

    // 3. Gestione eventi indirizzi
    addressRadios.forEach(radio => {
        radio.addEventListener('change', () => {
            aggiornaOrarioConsegna(radio.value);
        });
    });

    // 4. Gestione orario personalizzato
    customTimeRadio.addEventListener('change', function() {
        if (this.checked) {
            customTimeInput.style.display = 'block';
        }
    });

    defaultTimeRadio.addEventListener('change', function() {
        if (this.checked) {
            customTimeInput.style.display = 'none';
        }
    });

    // 5. Gestione submit
    window.submitCart = function() {
        const now = new Date();
        
        document.getElementById("cartDataInput").value = JSON.stringify(cart);
        document.getElementById("createdAtInput").value = now.toISOString();
        
        // Aggiorna l'orario di consegna
        if (customTimeRadio.checked && customTimeInput.value) {
            const customTime = new Date(customTimeInput.value);
            console.log("Stato radio button:", {
                customTimeChecked: customTimeRadio.checked,
                defaultTimeChecked: defaultTimeRadio.checked,
                customTimeValue: customTimeInput.value
            });
            customTime.setHours(customTime.getHours() + 2); //accorgimenti per il fuso orario
            timeInputHidden.value = customTime.toISOString().replace('T', ' ').slice(0, 16);
        }

        if (!validateDeliveryTime()) {
            return; // Blocca l'invio se la validazione fallisce
        }
        
        // Validazione
        const indirizzoSelezionato = document.querySelector('input[name="indirizzo_id"]:checked');
        const cartaSelezionata = document.querySelector('input[name="numero_carta"]:checked');
        
        if (!indirizzoSelezionato) {
            alert("Seleziona un indirizzo di consegna");
            return;
        }
        
        if (!cartaSelezionata) {
            alert("Seleziona un metodo di pagamento");
            return;
        }

        // Debug: mostra il valore che verrà inviato
        console.log("Dati che verranno inviati:", {
            cart_data: document.getElementById("cartDataInput").value,
            created_at: document.getElementById("createdAtInput").value,
            dataConsegna: document.getElementById("dataConsegna").value,
            note: document.getElementById("note").value,
            indirizzo_id: document.querySelector('input[name="indirizzo_id"]:checked')?.value,
            numero_carta: document.querySelector('input[name="numero_carta"]:checked')?.value
        });

        cartForm.submit();
    };

    // Inizializzazione
    const checkedRadio = Array.from(addressRadios).find(r => r.checked);
    if (checkedRadio) {
        aggiornaOrarioConsegna(checkedRadio.value);
    }
});