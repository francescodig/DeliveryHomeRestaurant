<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consegne - Nome Ristorante</title>
    <link rel="stylesheet" href="/resources/css/personale_consegne.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="/resources/css/layout.css">
</head>
<body>
    <#include "header.ftl">

    <main>
        <div class="deliveries-container">
            <#list orders as order>
                <div class="delivery-card">
                    <div class="delivery-header">
                        <h3>Ordine #${order.getId()}</h3>

                        <#-- Calcolo statoClasse -->
                        <#assign statoClasse = "">
                        <#if order.getStato() == "annullato">
                            <#assign statoClasse = "annullato">
                        <#elseif order.getStato() == "consegnato">
                            <#assign statoClasse = "consegnato">
                        <#elseif order.getStato() == "pronto">
                            <#assign statoClasse = "pronto">
                        <#elseif order.getStato() == "in_preparazione">
                            <#assign statoClasse = "in_preparazione">
                        <#elseif order.getStato() == "in_attesa">
                            <#assign statoClasse = "errore">
                        </#if>

                        <span class="order-status ${statoClasse?html}">${order.getStato()?replace("_", " ")?cap_first}</span>
                    </div>
                    <div class="delivery-info">
                        <p><strong>Note:</strong> ${order.getNote()?html}</p>
                        <p><strong>Data esecuzione:</strong> ${order.getDataEsecuzione()?string("dd/MM/yyyy HH:mm:ss")}</p>
                        <p><strong>Data ricezione:</strong> ${order.getDataRicezione()?string("dd/MM/yyyy HH:mm:ss")}</p>
                        <p><strong>Costo totale:</strong> €${order.getCosto()}</p>
                        <p><strong>Prodotti:</strong></p>
                        <ul>
                            <#list order.getProdotti() as prodotto>
                                <li>${prodotto.getNome()?html} - ${prodotto.getDescrizione()?html} - €${prodotto.getCosto()}</li>
                            </#list>
                        </ul>
                    </div>
                    <form method="POST" action="/Delivery/Rider/cambiaStatoOrdine" class="status-form">
                        <input type="hidden" name="ordineId" value="${order.getId()}">
                        <label for="status${order.getId()}">Modifica stato:</label>
                        <select name="stato" id="status${order.getId()}" class="status-select">
                            <option value="">-- Seleziona stato --</option>
                            <option value="annullato" <#if statoClasse == "annullato">selected</#if>>Annullato</option>
                            <option value="consegnato" <#if statoClasse == "consegnato">selected</#if>>Consegnato</option>
                            <option value="pronto" <#if statoClasse == "pronto">selected</#if>>Pronto</option>
                            <option value="in_preparazione" <#if statoClasse == "in_preparazione">selected</#if>>In Preparazione</option>
                            <option value="in_attesa" <#if statoClasse == "errore">selected</#if>>In Attesa</option>
                        </select>
                    </form>

