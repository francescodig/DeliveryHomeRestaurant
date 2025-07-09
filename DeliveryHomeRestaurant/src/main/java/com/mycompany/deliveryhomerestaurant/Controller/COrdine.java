/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;
import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECartaCreditoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EIndirizzoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EProdottoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import com.mycompany.deliveryhomerestaurant.Model.EItemOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.OrderTimeCalculator;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import com.mycompany.deliveryhomerestaurant.util.UtilityJSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author franc
 */
public class COrdine {

    public void showConfirmOrder(HttpServletRequest request, HttpServletResponse response, String[] params) throws IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
        String role = "";
        boolean logged = true;
        
        try{
            
                // Controllo login e ruolo
            if (session == null || session.getAttribute("utente") == null) {
                response.sendRedirect("/DeliveryHomeRestaurant/User/showProfile"); // Utente non autenticato
                logged = false;
                return;
            }

            ECliente client = (ECliente) session.getAttribute("utente");
            ECliente clientAttached = (ECliente) utenteDAO.findById(client.getId());
            role = clientAttached.getRuolo();


            // Recupero e parsing del carrello JSON
            String cartData = request.getParameter("cart_data");

            if (cartData == null || cartData.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello non inviato");
                return;
            }

            List<Map<String, Object>> cart;
            try {
                cart = UtilityJSON.parseCart(cartData); // helper statico per parsing JSON → lista di mappe
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello non valido");
                return;
            }

            if (cart.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello vuoto");
                return;
            }

            // Caricamento indirizzi e carte dell'utente

            List<EIndirizzo> adresses = clientAttached.getIndirizziConsegna();
            List<ECartaCredito> cards = clientAttached.getMetodiPagamento();


            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("carte_credito", cards);
            data.put("indirizzi", adresses);
            data.put("role", role);
            data.put("logged", logged);
            Template template = cfg.getTemplate("check_order.ftl");


            template.process(data, response.getWriter());

        
            
        } catch(Exception e){
            
             e.printStackTrace(); // oppure log.error("Errore", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            
            
        }

     }
    
    public void confirmPayment(HttpServletRequest request, HttpServletResponse response, String[] params)
             throws IOException, TemplateException {
        
        HttpSession session = UtilSession.getSession(request);
        EntityManager em = (EntityManager) request.getAttribute("em");
        EIndirizzoDAO indirizzoDAO = new EIndirizzoDAOImpl(em);
        ECartaCreditoDAO cartaCreditoDAO = new ECartaCreditoDAOImpl(em);
        EProdottoDAO prodottoDAO = new EProdottoDAOImpl(em);
        EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
        EntityTransaction transaction = null;
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        double totalPrice = 0.0;

        
        try{
            
            EUtente utente = (EUtente) session.getAttribute("utente");
            ECliente cliente = (ECliente) utenteDAO.findById(utente.getId());
            String cartJson = request.getParameter("cart_data");
            String note = request.getParameter("note");
            JSONArray cartArray = new JSONArray(cartJson);
            if (cartArray.isEmpty()) {
                throw new IllegalArgumentException("Carrello non valido o vuoto.");
            }
            
            String dataConsegnaStr = request.getParameter("dataConsegna");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dataConsegna = LocalDateTime.parse(dataConsegnaStr, formatter);
            
            int indirizzoId = Integer.parseInt(request.getParameter("indirizzo_id"));
            EIndirizzo indirizzoConsegna = indirizzoDAO.getAddressById(indirizzoId);
            
            String numeroCarta = request.getParameter("numero_carta");
            ECartaCredito metodoPagamento = cartaCreditoDAO.getCreditCardByCardNumber(numeroCarta);
            
            EOrdine ordine = new EOrdine();
            List<EItemOrdine> itemOrdineList = new ArrayList<>();


            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject item = cartArray.getJSONObject(i);

                
                int prodottoId = item.getInt("id");
                int qty = item.getInt("qty");
                EProdotto prodotto = prodottoDAO.getProductById(prodottoId);
                if (prodotto == null) {
                    throw new IllegalArgumentException("Prodotto " + item.getString("name") + " non trovato.");
                }

                EItemOrdine itemOrdine = new EItemOrdine();
                itemOrdine.setOrdine(ordine);
                itemOrdine.setProdotto(prodotto);
                itemOrdine.setQuantita(qty);
                itemOrdine.setPrezzoUnitario(item.getBigDecimal("price"));

                ordine.addItemOrdine(itemOrdine);
                itemOrdineList.add(itemOrdine); 

                totalPrice += item.getDouble("price") * item.getInt("qty");
            }
            
            ordine.setDataEsecuzione(LocalDateTime.now());
            ordine.setDataRicezione(LocalDateTime.now());
            ordine.setCosto(BigDecimal.valueOf(totalPrice));
            ordine.setCliente(cliente);
            ordine.setStato("in_attesa");
            ordine.setNote(note);
            ordine.setIndirizzoConsegna(indirizzoConsegna);
            ordine.setCartaPagamento(metodoPagamento);
            
                    // Inizio Transazione
        transaction = em.getTransaction();
        transaction.begin();
        em.persist(ordine);
        for (EItemOrdine itemOrdine : itemOrdineList) {
            em.persist(itemOrdine);
        }
        em.flush();
        transaction.commit();
        // Fine Transazione
        Template template = cfg.getTemplate("confirmed_order.ftl");
        
        Map<String, Object> data = new HashMap();
        
        data.put("contextPath", request.getContextPath());
        
        template.process(data, response.getWriter());
        
        

    } catch (IllegalArgumentException e) {
        if(transaction != null){
            transaction.rollback();
        }
        System.err.println("Errore input utente: " + e.getMessage());
        throw e;
    } catch (Exception e) {
        if(transaction != null){
            transaction.rollback();
        }
        System.err.println("Errore database: " + e.getMessage());
        throw e;
    }

            

            
            

        
        
        
        


    }
    
    public void getEstimatedTime(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, ServletException {

    EntityManager em = (EntityManager) request.getAttribute("em");
    EIndirizzoDAO indirizzoDAO = new EIndirizzoDAOImpl(em);
    EProdottoDAO prodottoDAO = new EProdottoDAOImpl(em);
    EOrdineDao ordineDAO = new EOrdineDAOImpl(em);

    try {
        int indirizzoId = Integer.parseInt(request.getParameter("indirizzo_id"));
        EIndirizzo indirizzo = indirizzoDAO.getAddressById(indirizzoId);
        String indirizzoCliente = indirizzo.getVia() + " " + indirizzo.getCivico() + ", " + indirizzo.getCitta();

        String cartJson = request.getParameter("cart_data");
        JSONArray cartArray = new JSONArray(cartJson);

        if (cartArray.length() == 0) {
            throw new IllegalArgumentException("Carrello non valido");
        }

        List<EItemOrdine> itemOrderList = new ArrayList<>();
        EOrdine ordine = new EOrdine();

        for (int i = 0; i < cartArray.length(); i++) {
            JSONObject item = cartArray.getJSONObject(i);
            int id = item.getInt("id");
            int qty = item.getInt("qty");
            BigDecimal price = item.getBigDecimal("price");

            EProdotto prodotto = prodottoDAO.getProductById(id);
            if (prodotto == null) {
                throw new IllegalArgumentException("Prodotto con ID " + id + " non trovato");
            }

            EItemOrdine itemOrder = new EItemOrdine();
            itemOrder.setOrdine(ordine);
            itemOrder.setProdotto(prodotto);
            itemOrder.setQuantita(qty);
            itemOrder.setPrezzoUnitario(price);
            itemOrderList.add(itemOrder);
        }

        int numeroOrdini;
        try {
            List<EOrdine> ordiniAttivi = ordineDAO.getOrdersByState("in_preparazione");
            numeroOrdini = ordiniAttivi != null ? ordiniAttivi.size() : 10;
        } catch (Exception e) {
            numeroOrdini = 10;
        }

        OrderTimeCalculator orderTime = new OrderTimeCalculator();
        LocalDateTime estimatedTime = orderTime.orarioConsegnaCalculator(itemOrderList, numeroOrdini, indirizzoCliente);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String formattedTime = estimatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        String json = String.format("{\"estimated_time\": \"%s\"}", formattedTime);

        response.getWriter().write(json);

    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorJson = String.format("{\"error\": \"%s\"}", e.getMessage().replace("\"", "\\\""));
        response.getWriter().write(errorJson);
    }
}

    
   
    
}
