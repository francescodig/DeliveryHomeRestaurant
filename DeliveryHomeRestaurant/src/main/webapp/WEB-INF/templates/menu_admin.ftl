<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Gestione Menu | Home Restaurant</title>
    <link rel="stylesheet" href="/resources/css/layout.css">
    <link rel="stylesheet" href="/resources/css/menu_admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Header -->
    {include file="header.tpl"}

    <!-- Main Content -->
    <main class="admin-container">
        <div class="admin-header">
        <a href="/Delivery/Proprietario/showPanel" class="back-button">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-utensils"></i> Gestione Menu</h1>
            <p class="admin-subtitle">Gestisci tutti i prodotti del tuo menu</p>
        </div>
        
        <!-- Filtri e Ricerca -->
        <section class="filters-section">
            <div class="filters-grid">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="searchProducts" placeholder="Cerca prodotti...">
                </div>
                <div class="filter-group">
                    <label for="filterCategory"><i class="fas fa-filter"></i> Filtra per categoria:</label>
                    <select id="filterCategory">
                        <option value="all">Tutte le categorie</option> 
                        <option value="antipasti">Antipasti</option> 
                        <option value="primi">Primi</option>
                        <option value="secondi">Secondi</option>
                        <option value="dolci">Dolci</option>
                        <option value="bevande">Bevande</option>
                    </select>
                </div>
            </div>
        </section>

        <!-- Lista Prodotti -->
        <section class="products-list">
            <div class="products-header">
                <h2><i class="fas fa-list"></i> Tutti i Prodotti</h2>
            </div>
            
            {if $products|@count > 0}
                <table class="products-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Descrizione</th>
                            <th>Categoria</th>
                            <th>Prezzo</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        {foreach from=$products item=product}
                            <tr data-id="{$product->getId()}">
                                <td>{$product->getId()}</td>
                                <td>{$product->getNome()}</td>
                                <td>{$product->getDescrizione()}</td>
                                <td>{$product->getCategoria()->getNome()}</td>
                                <td>€{$product->getCosto()|number_format:2}</td>
                                <td class="actions">
                                    <button class="btn btn-edit" data-id="{$product->getId()}">
                                        <i class="fas fa-edit"></i> Modifica
                                    </button>
                                    <form action="/Delivery/Proprietario/METODOELIMINAPRODOTTO" method="post" class="inline-delete-form">
                                        <input type="hidden" name="product_id" value="{$product->getId()}">
                                        <button type="submit" class="btn btn-delete">
                                            <i class="fas fa-trash-alt"></i> Elimina
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        {/foreach}
                    </tbody>
                </table>
            {else}
                <div class="no-products">
                    <i class="far fa-frown"></i>
                    <p>Nessun prodotto trovato</p>
                </div>
            {/if}
        </section>

        <!-- Form Aggiungi Prodotto -->
        <section class="product-form-section">
            <h2><i class="fas fa-plus-circle"></i> Aggiungi Prodotto</h2>
            
            <form id="productForm" action="/Delivery/Proprietario/NOMEDELMETODOPERAGGIUNGEREUNPRODOTTO" method="post">
                {if $editMode}
                    <input type="hidden" name="product_id" value="{$editingProduct->getId()}">
                {/if}
                
                <div class="form-grid">
                    <div class="form-group">
                        <label for="productName">Nome:</label>
                        <input type="text" id="productName" name="nome" value="{if $editMode}{$editingProduct->getNome()}{/if}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="productCategory">Categoria:</label>
                        <select id="productCategory" name="categoria_id" required>
                            {foreach from=$categories item=category}
                                <option value="{$category->getId()}" {if $editMode && $editingProduct->getCategoria()->getId() == $category->getId()}selected{/if}>
                                    {$category->getNome()}
                                </option>
                            {/foreach}
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="productPrice">Prezzo (€):</label>
                        <input type="number" id="productPrice" name="costo" step="0.01" min="0" value="{if $editMode}{$editingProduct->getCosto()}{/if}" required>
                    </div>
                    
                    <div class="form-group full-width">
                        <label for="productDescription">Descrizione:</label>
                        <textarea id="productDescription" name="descrizione" rows="3" required>{if $editMode}{$editingProduct->getDescrizione()}{/if}</textarea>
                    </div>
                    
                    <div class="form-group full-width actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> {if $editMode}Salva Modifiche{else}Aggiungi Prodotto{/if}
                        </button>
                        
                        {if $editMode}
                            <a href="/Delivery/Proprietario/gestioneMenu" class="btn btn-cancel">
                                <i class="fas fa-times"></i> Annulla
                            </a>
                        {/if}
                    </div>
                </div>
            </form>
        </section>
    </main>

    <!-- Footer -->
    {include file="footer.tpl"}

    <!-- Modal Conferma Eliminazione -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <h2><i class="fas fa-exclamation-triangle"></i> Conferma Eliminazione</h2>
            <p>Sei sicuro di voler eliminare questo prodotto?</p>
            <div class="modal-actions">
                <button id="confirmDelete" class="btn btn-danger">Elimina</button>
                <button id="cancelDelete" class="btn btn-secondary">Annulla</button>
            </div>
        </div>
    </div>

    <script src="/resources/js/hamburger.js"></script>
    <script src="/resources/js/theme.js"></script>
</body>
</html>