document.addEventListener('DOMContentLoaded', () => { //quando html è caricato
  const themeToggle = document.getElementById('theme-toggle'); //cerca il bottone per il cambio tema
  if (!themeToggle) return; 


  const savedTheme = localStorage.getItem('theme');
  const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  const initialTheme = savedTheme || (systemPrefersDark ? 'dark' : 'light'); //cera nel localStorage la preferenza, altrimenti default

  setTheme(initialTheme); //imposta il tema scelto 

  themeToggle.addEventListener('click', () => { //qunado clicco cambia il tema e lo salva in localStrorage
    const currentTheme = document.body.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    setTheme(newTheme);
    localStorage.setItem('theme', newTheme);
  });

  function setTheme(theme) {
    document.body.setAttribute('data-theme', theme);
    updateButton(theme); //cambia il data-theme nel body e aggiorna l'icona
  }

  function updateButton(theme) { //funzione chiamata prima per cambiare icona
    if (theme === 'dark') {
      themeToggle.innerHTML = '<i class="fas fa-sun"></i> <span class="toggle-text">Chiaro</span>';
    } else {
      themeToggle.innerHTML = '<i class="fas fa-moon"></i> <span class="toggle-text">Scuro</span>';
    }
  } 
});
