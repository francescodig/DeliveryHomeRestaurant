<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Gestione Menu | Home Restaurant</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/layout.css">
    <link rel="stylesheet" href="${contextPath}/resources/css/menu_admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Header -->
    <#include "header.ftl">

    <!-- Main Content -->
    <main class="admin-container">
        <div class="admin-header">
        <a href="${contextPath}/Proprietario/showPanel" class="back-button">
                <i class="fas fa-arrow-left"></i>
            </a>
            <h1><i class="fas fa-utensils"></i> Gestione Menu</h1>
            <p class="admin-subtitle">Gestisci tutti i prodotti del tuo menu</p>
        </div>
        
        <!-- Filtri e Ricerca -->
        <section class="filters-section">
        <form method="get" action="${contextPath}/Proprietario/showMenu" id="filterForm">
            <div class="filters-grid">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" name="search" value="${search?if_exists?html}" placeholder="Cerca prodotti...">
                </div>
                <div class="filter-group">
                    <label for="filterCategory"><i class="fas fa-filter"></i> Filtra per categoria:</label>
                    <select id="filterCategory" name="category" onchange="document.getElementById('filterForm').submit()">
                        <option value="all" <#if category == "all">selected</#if>>Tutte le categorie</option>
                        <#list categorie as categoria>
                            <option value="${categoria.nome?html}" 
                                <#if category?? && category == categoria.nome>selected</#if>>
                                ${categoria.nome?cap_first}
                            </option>
                        </#list>
                    </select>
                    <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i></button>
                </div>
            </div>
            </form>
        </section>

        <!-- Lista Prodotti -->
        <section class="products-list">
            <div class="products-header">
                <h2><i class="fas fa-list"></i> Tutti i Prodotti</h2>
            </div>
            
            <#if (prodotti?size > 0)>
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
                        <#list prodotti as product>
                            <tr data-id="${product.id}">
                                <td data-label="ID">${product.id}</td>
                                <td data-label="Nome">${product.nome?html}</td>
                                <td data-label="Descrizione">${product.descrizione?html}</td>
                                <td data-label="Categoria">${product.categoria.nome?html}</td>
                                <td data-label="Prezzo">€${product.costo?string["0.00"]}</td>
                                <td class="actions">
                                    <button class="btn btn-edit" data-id="${product.id}">
                                        <i class="fas fa-edit"></i> Modifica
                                    </button>
                                    <form action="${contextPath}/Proprietario/deleteProduct" method="post" class="inline-delete-form">
                                        <input type="hidden" name="product_id" value="${product.id}">
                                        <button type="submit" class="btn btn-delete">
                                            <i class="fas fa-trash-alt"></i> Elimina
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <div class="no-products">
                    <i class="far fa-frown"></i>
                    <p>Nessun prodotto trovato</p>
                </div>
            </#if>
        </section>

        <!-- Form Aggiungi Prodotto -->
       <section class="product-form-section">
        <h2><i class="fas fa-plus-circle"></i> Aggiungi Prodotto</h2>

        <form id="productForm" action="${contextPath}/Proprietario/saveProduct" method="post">
        
        <#if (editMode!false) == true && (editingProduct??)>
            <input type="hidden" name="product_id" value="${editingProduct.id!}">
        </#if>

        <div class="form-grid">
            <div class="form-group">
                <label for="productName">Nome:</label>
                <input type="text" id="productName" name="nome" 
                       value="<#if (editMode!false) == true && (editingProduct??)>${editingProduct.nome!?html}</#if>" required>
            </div>

            <div class="form-group">
                <label for="productCategory">Categoria:</label>
                <select id="productCategory" name="categoria_id" required>
                    <#if categories??>
                        <#list categories as category>
                            <option value="${category.id!}"
                                <#if (editMode!false) == true && (editingProduct??) && (editingProduct.categoria??) && (editingProduct.categoria.id == category.id)>
                                    selected
                                </#if>>
                                ${category.nome!?html}
                            </option>
                        </#list>
                    <#else>
                        <option value="">Nessuna categoria disponibile</option>
                    </#if>
                </select>
            </div>

            <div class="form-group">
                <label for="productPrice">Prezzo (€):</label>
                <input type="number" id="productPrice" name="costo" step="0.01" min="0"
                       value="<#if (editMode!false) == true && (editingProduct??)>${editingProduct.costo!?string['0.##']}</#if>" required>
            </div>

            <div class="form-group full-width">
                <label for="productDescription">Descrizione:</label>
                <textarea id="productDescription" name="descrizione" rows="3" required><#if (editMode!false) == true && (editingProduct??)>${editingProduct.descrizione!?html}</#if></textarea>
            </div>

            <div class="form-group full-width actions">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> <#if (editMode!false) == true>Salva Modifiche<#else>Aggiungi Prodotto</#if>
                </button>

                <#if (editMode!false) == true>
                    <a href="${contextPath}/Proprietario/gestioneMenu" class="btn btn-cancel">
                        <i class="fas fa-times"></i> Annulla
                    </a>
                </#if>
            </div>
        </div>
    </form>

    </section>

    </main>

    <!-- Footer -->
    <#include "footer.ftl">

    <script src="${contextPath}/resources/Js/hamburger.js"></script>
    <script src="${contextPath}/resources/Js/theme.js"></script>
</body>
</html>