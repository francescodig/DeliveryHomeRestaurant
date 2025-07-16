/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import org.apache.commons.text.StringEscapeUtils;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import com.mycompany.deliveryhomerestaurant.Model.EItemOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.AccessControlUtil;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
import com.mycompany.deliveryhomerestaurant.util.UtilFlashMessages;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author simone
 */
public class CChef {
    
    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
    throws IOException, TemplateException, ServletException {
        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        String role = "";
        boolean logged = true;
        try {
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ECuoco cuoco = AccessControlUtil.checkUserRole(utente, ECuoco.class);
            role = cuoco.getRuolo();

            List<EOrdine> ordiniChef = ordineDAO.getOrdersByState("in_preparazione");

            Map<String, Object> data = new HashMap<>();
            data.put("orders", ordiniChef);
            data.put("role", role);
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "chef_orders.ftl", data);

        } catch (SecurityException e) {
            logged = false;
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }

    public void cambiaStatoOrdine(HttpServletRequest request, HttpServletResponse response, String[] params)
    throws IOException, TemplateException, ServletException, IllegalArgumentException {
        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        String role = "";
        boolean logged = true;
        try {
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ECuoco cuoco = AccessControlUtil.checkUserRole(utente, ECuoco.class);
            role = cuoco.getRuolo();

        String ordineId = request.getParameter("ordineId");
        String nuovoStato = request.getParameter("stato");
        String statoAttuale = request.getParameter("stato_attuale");
        EOrdine ordine = ordineDAO.getOrdineById(ordineId);
        

        em.getTransaction().begin();
        
        if (statoAttuale == null ? ordine.getStato() != null : !statoAttuale.equals(ordine.getStato())){
            
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Ordine già modificato da un altro utente");
            }
            if(("in_preparazione".equals(nuovoStato) && !"in_attesa".equals(ordine.getStato())) || ("pronto".equals(nuovoStato) && !"in_preparazione".equals(ordine.getStato()))){
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Ordine già modificato da un altro utente");
            }


        
        ordine.setStato(nuovoStato);
        if ("pronto".equals(nuovoStato)) {
            ordine.setDataConsegna(LocalDateTime.now());
        }
        em.flush();
        em.getTransaction().commit();
        UtilFlashMessages.addMessage(request, "success", "Ordine modificato con successo");
        response.sendRedirect(request.getContextPath() + "/Chef/showOrders");

    } catch (SecurityException e) {
        logged = false;
        TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
    } catch(IllegalArgumentException e){
        UtilFlashMessages.addMessage(request, "error", e.getMessage());
        response.sendRedirect(request.getContextPath() + "/Chef/showOrders/");
    } catch (Exception e) {
        TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
    }
}
    
    public void showOrdersInAttesa(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws IOException, TemplateException, ServletException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        String role = "";
        boolean logged = true;

    try {
        EUtente utente = AccessControlUtil.getLoggedUser(request);
        ECuoco cuoco = AccessControlUtil.checkUserRole(utente, ECuoco.class);
        role = cuoco.getRuolo();

        List<EOrdine> ordiniChef = ordineDAO.getOrdersByState("in_attesa");

        Map<String, Object> data = new HashMap<>();
        data.put("orders", ordiniChef);
        data.put("role", role);
        Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
        data.put("messages", messages);

        TemplateRenderer.render(request, response, "waiting_orders.ftl", data);

    } catch (SecurityException e) {
        logged = false;
        TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
    } catch(IllegalArgumentException e){
        TemplateRenderer.mostraErrore(request, response, "chef_error.ftl", e.getMessage(), role, logged);
    } catch (Exception e) {
        TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
    }
        
    }
    
    public void accettaOrdine(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws IOException, ServletException, TemplateException{
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        
        String role = "";
        boolean logged = true;
        
        String ordineId = request.getParameter("ordine_id");
        
        em.getTransaction().begin();
        
        
        try{
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ECuoco cuoco = AccessControlUtil.checkUserRole(utente, ECuoco.class);
            role = cuoco.getRuolo();
            
            EOrdine ordine = em.find(EOrdine.class, ordineId, LockModeType.PESSIMISTIC_WRITE);
            if(!"in_attesa".equals(ordine.getStato())){
                throw new IllegalArgumentException("L'ordine selezionato è già stato modificato da un altro dipendente!");
            }
            
            ordine.setStato("in_preparazione");
            
            ECliente cliente = ordine.getCliente();
            String email  = cliente.getEmail();
            String nome = cliente.getNome();
            
            int orderId = ordine.getId();
            List<EItemOrdine> prodotti = ordine.getItemOrdini();
            LocalDateTime dataOrdine = ordine.getDataEsecuzione();
            BigDecimal costo = ordine.getCosto();
            EIndirizzo indirizzo = ordine.getIndirizzoConsegna();
            ECartaCredito pagamento = ordine.getCartaPagamento();
            


            StringBuilder listaProdotti = new StringBuilder("<ul>");
            for (EItemOrdine item : prodotti) {
                EProdotto prodotto = item.getProdotto();
                String nomeProdotto = StringEscapeUtils.escapeHtml4(prodotto.getNome());
                int quantita = item.getQuantita();
                BigDecimal prezzo = item.getPrezzoUnitario();

                listaProdotti.append("<li>")
                             .append(nomeProdotto)
                             .append(" - qty: ")
                             .append(quantita)
                             .append(" - €")
                             .append(prezzo)
                             .append("</li>");
            }
            listaProdotti.append("</ul>");
            
            
        em.flush();
        em.getTransaction().commit();
        
        UtilFlashMessages.addMessage(request, "success", "Ordine accettato con successo");
        response.sendRedirect(request.getContextPath() + "/Chef/showOrdersInAttesa");
        
        


            
            
            
            
            
            
            
        } catch (SecurityException e) {
            logged = false;
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        }  catch(IllegalArgumentException e){
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Chef/showOrdersInAttesa");
        } catch(Exception e){
            
            TemplateRenderer.mostraErrore(request, response, "chef_error.ftl", e.getMessage(), role, logged);
            
        }
        
        
        
        
        
        
    }

    
    
    public void rifiutaOrdine(HttpServletRequest request, HttpServletResponse response, String[] params)
                throws IOException, ServletException, TemplateException{
        
        
        
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        
        String role = "";
        boolean logged = true;
        
        String ordineId = request.getParameter("ordine_id");
        String motivazioneRifiuto = request.getParameter("motivazione_rifiuto");
        
        em.getTransaction().begin();
        
        try{

            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ECuoco cuoco = AccessControlUtil.checkUserRole(utente, ECuoco.class);
            role = cuoco.getRuolo();
            
            EOrdine ordine = em.find(EOrdine.class, ordineId, LockModeType.PESSIMISTIC_WRITE);
            
            if(!ordine.getStato().equals("in_attesa")){
                throw new IllegalArgumentException("L'ordine selezionato è già stato modificato da un altro dipendente!");
            }
            
            ordine.setStato("annullato");
            
    
            
            ECliente cliente = ordine.getCliente();
            String email  = cliente.getEmail();
            String nome = cliente.getNome();
            
            int orderId = ordine.getId();
            List<EItemOrdine> prodotti = ordine.getItemOrdini();
            LocalDateTime dataOrdine = ordine.getDataEsecuzione();
            BigDecimal costo = ordine.getCosto();
            EIndirizzo indirizzo = ordine.getIndirizzoConsegna();
            ECartaCredito pagamento = ordine.getCartaPagamento();
            
            
            StringBuilder listaProdotti = new StringBuilder("<ul>");
            for (EItemOrdine item : prodotti) {
                EProdotto prodotto = item.getProdotto();
                String nomeProdotto = StringEscapeUtils.escapeHtml4(prodotto.getNome());
                int quantita = item.getQuantita();
                BigDecimal prezzo = item.getPrezzoUnitario();

                listaProdotti.append("<li>")
                             .append(nomeProdotto)
                             .append(" - qty: ")
                             .append(quantita)
                             .append(" - €")
                             .append(prezzo)
                             .append("</li>");
            }
            listaProdotti.append("</ul>");

      
            
            em.flush();
            em.getTransaction().commit();
        
            UtilFlashMessages.addMessage(request, "success", "Ordine rifiutato con successo");
            response.sendRedirect(request.getContextPath() + "/Chef/showOrdersInAttesa");
            
            
        } catch (SecurityException e) {
            logged = false;
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        }  catch(IllegalArgumentException e){
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Chef/showOrdersInAttesa");
        } catch(Exception e){
            
            TemplateRenderer.mostraErrore(request, response, "chef_error.ftl", e.getMessage(), role, logged);
            
        }
        
        
        
        
    }

}





    

