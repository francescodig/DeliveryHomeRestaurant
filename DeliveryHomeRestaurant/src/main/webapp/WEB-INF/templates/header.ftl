<header>
    <div class="header-container">

        <!-- Hamburger visibile solo su mobile -->
        <button type="button" class="hamburger" id="hamburger">&#9776;</button>

        <a href="/DeliveryHomeRestaurant/User/home/" class="logo">
            <img src="${contextPath}/resources/Immagini/logo.png" alt="Logo">
        </a>

        <div class="nav-links" id="nav-menu">
            <a href="/DeliveryHomeRestaurant/User/home/">Home</a>
            <a href="/DeliveryHomeRestaurant/User/mostraMenu/">Menù</a>

            <#if role?? && (role == "cliente" || role == "")>
                <a href="/DeliveryHomeRestaurant/User/order/">Ordina</a>
            </#if>

            <#if role?? && role == "rider">
                <a href="/DeliveryHomeRestaurant/Rider/showOrders/">Ordini Consegna</a>
            </#if>

            <#if role?? && role == "cuoco">
                <a href="/DeliveryHomeRestaurant/Chef/showOrders/">Ordini In Cucina</a>
            </#if>

            <#if logged?? && (logged && role == "cliente")>
                <a href="/DeliveryHomeRestaurant/User/showMyOrders/">I Miei Ordini</a>
            </#if>

            <#if logged?? && (logged && role == "proprietario")>
                <a href="/DeliveryHomeRestaurant/Proprietario/showPanel/">Pannello di Controllo</a>
            </#if>
        </div>

        <div class="user-actions">
            <a href="admin_panel.html" title="Notifiche">
                <i class="fas fa-bell"></i>
            </a>
            <a href="/DeliveryHomeRestaurant/User/showProfile" title="Profilo">
                <i class="fas fa-user"></i>
            </a>
            <button id="theme-toggle" class="theme-toggle" aria-label="Cambia tema">
                <i class="fas fa-moon"></i>
                <span class="toggle-text">Scuro</span>
            </button>
        </div>
    </div>

    <script src="${contextPath}/resources/Js/hamburger.js" defer></script>
</header>
