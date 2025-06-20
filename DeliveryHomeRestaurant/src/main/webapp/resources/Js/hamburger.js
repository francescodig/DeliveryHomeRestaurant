//Comportamento hamburger per mobile
function setupMobileMenu() {
    console.log("setupMobileMenu called");
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('nav-menu');

    if (hamburger && navMenu) {
        hamburger.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopImmediatePropagation();
            navMenu.classList.toggle('active');
            this.innerHTML = navMenu.classList.contains('active') ? '✕' : '&#9776;';
            document.body.style.overflow = navMenu.classList.contains('active') ? 'hidden' : '';
        });

        navMenu.querySelectorAll('a').forEach(link => {
            link.addEventListener('click', () => {
                navMenu.classList.remove('active');
                hamburger.innerHTML = '&#9776;';
                document.body.style.overflow = '';
            });
        });
    }
}

// Attiva quando l'header è stato caricato
document.addEventListener('DOMContentLoaded', setupMobileMenu);

