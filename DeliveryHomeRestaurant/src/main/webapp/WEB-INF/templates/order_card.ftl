<div class="delivery-card">
    <div class="delivery-header">
        <h3>Ordine #${order.id}</h3>

        <#-- Determina la classe dello stato -->
        <#assign statoClasse = "">
        <#if order.stato == "annullato">
            <#assign statoClasse = "annullato">
        <#elseif order.stato == "consegnato">
            <#assign statoClasse = "consegnato">
        <#elseif order.stato == "pronto">
            <#assign statoClasse = "pronto">
        <#elseif order.stato == "in_preparazione">
            <#assign statoClasse = "in_preparazione">
        <#elseif order.stato == "in_attesa">
            <#assign statoClasse = "errore">
        <#elseif order.stato == "in_consegna">
            <#assign statoClasse = "in_consegna">
        </#if>

        <span class="order-status ${statoClasse?html}">
            ${order.stato?replace("_", " ")?capitalize}
        </span>
    </div>

    <div class="delivery-info">
        <#if order.note?? && (order.note?length > 0)>
            <p><strong>Note:</strong> ${order.note?html}</p>
        <#else>
            <p><strong>Note:</strong> Nessuna nota</p>
        </#if>
        <p><strong>Data esecuzione:</strong> ${order.dataEsecuzione}</p>
        <p><strong>Data ricezione:</strong> ${order.dataRicezione}</p>
        <p><strong>Costo totale:</strong> €${order.costo}</p>
        <p><strong>Prodotti:</strong></p>
        <ul>
            <#list order.itemOrdini as itemOrdine>
                <li>
                    ${itemOrdine.prodotto.nome?html} -
                    ${itemOrdine.prodotto.descrizione?html} -
                    qty: ${itemOrdine.quantita} -
                    €${itemOrdine.prezzoUnitarioAlMomento}
                </li>
            </#list>
        </ul>
    </div>

    <form method="POST" action="${contextPath}/Rider/cambiaStatoOrdine" class="status-form">
        <input type="hidden" name="ordineId" value="${order.id}">
        <label for="status${order.id}">Modifica stato:</label>
        <select name="stato" id="status${order.id}" class="status-select">
            <option value="">-- Seleziona stato --</option>
            <#if statoClasse == "pronto">
                <option value="pronto" selected>Pronto</option>
            </#if>
            <option value="in_consegna" <#if statoClasse == "in_consegna">selected</#if>>In Consegna</option>
            <option value="consegnato" <#if statoClasse == "consegnato">selected</#if>>Consegnato</option>

            <#if showExtraStatuses?? && showExtraStatuses>
                <option value="in_preparazione" <#if statoClasse == "in_preparazione">selected</#if>>In Preparazione</option>
                <option value="in_attesa" <#if statoClasse == "errore">selected</#if>>In Attesa</option>
            </#if>
        </select>
    </form>
</div>
