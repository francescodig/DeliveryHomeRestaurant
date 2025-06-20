let currentForm = null;
let currentStatusElement = null;
let newStatusValue = '';
let oldStatusValue = '';

document.addEventListener('DOMContentLoaded', function () {
    const statusSelects = document.querySelectorAll('.status-select');

    statusSelects.forEach(select => {
        select.addEventListener('focus', function () {
            oldStatusValue = this.value;
        });

        select.addEventListener('change', function () {
            currentForm = this.closest('form');
            currentStatusElement = this.closest('.delivery-card').querySelector('.order-status');

            newStatusValue = this.value;
            document.getElementById('modalStatus').textContent = this.options[this.selectedIndex].text;

            this.value = oldStatusValue;

            document.getElementById('confirmModal').classList.add('show');
        });
    });

    document.getElementById('confirmBtn').addEventListener('click', function () {
        if (currentForm && newStatusValue) {
            const selectElement = currentForm.querySelector('.status-select');

            selectElement.value = newStatusValue;
            currentStatusElement.textContent = selectElement.options[selectElement.selectedIndex].text;

            currentStatusElement.classList.remove('in-consegna', 'consegnato', 'errore', 'da-ritirare', 'annullato', 'pronto', 'in_preparazione', 'in_attesa');
            currentStatusElement.classList.add(newStatusValue);

            currentForm.submit();
        }
        document.getElementById('confirmModal').classList.remove('show');

        currentForm = null;
        currentStatusElement = null;
        newStatusValue = '';
        oldStatusValue = '';
    });
});

function closeModal() {
    document.getElementById('confirmModal').classList.remove('show');

    currentForm = null;
    currentStatusElement = null;
    newStatusValue = '';
    oldStatusValue = '';
}
