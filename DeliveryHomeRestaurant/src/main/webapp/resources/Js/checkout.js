document.addEventListener('DOMContentLoaded', function() {
    // Mostra/nascondi form nuova carta
    const addPaymentBtn = document.getElementById('add-payment');
    const newCardForm = document.querySelector('.new-card-form');
    
    addPaymentBtn.addEventListener('click', function() {
        newCardForm.classList.toggle('hidden');
        if (!newCardForm.classList.contains('hidden')) {
            newCardForm.scrollIntoView({ behavior: 'smooth' });
        }
    });
    
    // Mostra/nascondi form nuovo indirizzo
    const addAddressBtn = document.getElementById('add-address');
    const newAddressForm = document.querySelector('.new-address-form');
    
    addAddressBtn.addEventListener('click', function() {
        newAddressForm.classList.toggle('hidden');
        if (!newAddressForm.classList.contains('hidden')) {
            newAddressForm.scrollIntoView({ behavior: 'smooth' });
        }
    });
    
    // Seleziona carta/indirizzo
    document.querySelectorAll('.card-option, .address-option').forEach(option => {
        option.addEventListener('click', function() {
            // Rimuovi selected da tutti gli elementi dello stesso tipo
            const siblings = this.parentElement.querySelectorAll(`.${this.classList[0]}`);
            siblings.forEach(sib => sib.classList.remove('selected'));
            
            // Aggiungi selected a quello cliccato
            this.classList.add('selected');
            
            // Seleziona il radio button corrispondente
            const radio = this.querySelector('input[type="radio"]');
            if (radio) radio.checked = true;
        });
    });
    
    // Conferma ordine
    const confirmBtn = document.querySelector('.confirm-order');
    confirmBtn.addEventListener('click', function() {
        // Qui potresti aggiungere la logica per inviare l'ordine
        alert('Ordine confermato! Grazie per il tuo acquisto.');
        // window.location.href = 'ordine-confermato.html';
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const asapRadio = document.getElementById('asap');
    const scheduleRadio = document.getElementById('schedule');
    const scheduledTimeInput = document.getElementById('scheduled-time');

    function toggleScheduledTime() {
        if (scheduleRadio.checked) {
            scheduledTimeInput.disabled = false;
            scheduledTimeInput.focus();
        } else {
            scheduledTimeInput.disabled = true;
            scheduledTimeInput.value = '';
        }
    }

    // Usa sia change che click per gestire bene la selezione
    asapRadio.addEventListener('change', toggleScheduledTime);
    scheduleRadio.addEventListener('change', toggleScheduledTime);
    
    asapRadio.addEventListener('click', toggleScheduledTime);
    scheduleRadio.addEventListener('click', toggleScheduledTime);

    // Inizializza stato all'avvio
    toggleScheduledTime();
});


