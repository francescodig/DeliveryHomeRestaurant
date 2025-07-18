/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;
import com.mycompany.deliveryhomerestaurant.DAO.ECalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EExceptionCalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECartaCreditoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EExceptionCalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EIndirizzoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EProdottoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECalendario;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EExceptionCalendario;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import com.mycompany.deliveryhomerestaurant.Model.EItemOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.OrderTimeCalculator;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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


            // Recupero del carrello JSON
            String cartData = request.getParameter("cart_data");

            if (cartData == null || cartData.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello non inviato");
                return;
            }

            List<Map<String, Object>> cart;
            try {
                cart = UtilityJSON.parseCart(cartData); // metodo utilitario per lettura JSON e parsing in oggetti
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello non valido");
                return;
            }

            if (cart.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Carrello vuoto");
                return;
            }



            List<EIndirizzo> adresses = clientAttached.getActiveIndirizziConsegna();
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
            
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
            
            
        }

     }
    
   public void confirmPayment(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, TemplateException, Exception {

    HttpSession session = UtilSession.getSession(request);
    EntityManager em = (EntityManager) request.getAttribute("em");
    EIndirizzoDAO indirizzoDAO = new EIndirizzoDAOImpl(em);
    ECartaCreditoDAO cartaCreditoDAO = new ECartaCreditoDAOImpl(em);
    ECalendarioDAO calendarioDAO = new ECalendarioDAOImpl(em);
    EExceptionCalendarioDAO exceptionCalendarioDAO = new EExceptionCalendarioDAOImpl(em);
    EProdottoDAO prodottoDAO = new EProdottoDAOImpl(em);
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
    EntityTransaction transaction = null;
    Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
    String role = "";
    Boolean logged = true;
    double totalPrice = 0.0;

    try {


        EUtente utente = (EUtente) session.getAttribute("utente");
        role = utente.getRuolo();


        ECliente cliente = (ECliente) utenteDAO.findById(utente.getId());

        String cartJson = request.getParameter("cart_data");

        JSONArray cartArray = new JSONArray(cartJson);

        if (cartArray.isEmpty()) {
            throw new IllegalArgumentException("Carrello non valido o vuoto.");
        }


        String note = request.getParameter("note");
        String dataConsegnaStr = request.getParameter("dataConsegna");

        String dataConsegnaStrFormatted = dataConsegnaStr.replace(" ", "T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dataConsegna = LocalDateTime.parse(dataConsegnaStrFormatted, formatter);


        DayOfWeek nomeGiorno = dataConsegna.getDayOfWeek();


        ECalendario giorno = calendarioDAO.getDayById(nomeGiorno);
        LocalTime orarioApertura = giorno.getOrarioApertura();
        LocalTime orarioChiusura = giorno.getOrarioChiusura();

        if(!calendarioDAO.getDayById(nomeGiorno).isAperto()){
            throw new IllegalArgumentException("Il ristorante è chiuso in questo giorno.");
        }
        
        if (orarioApertura == null || orarioChiusura == null) {
            throw new IllegalArgumentException("Il ristorante è chiuso in questo giorno.");
        }


        List<EExceptionCalendario> giorniChiusuraEccezionali = exceptionCalendarioDAO.getGiorniChiusureStraordinarie();

        for (EExceptionCalendario giornoChiusura : giorniChiusuraEccezionali) {
            LocalDate giornoChiusuraDate = giornoChiusura.getExceptionDate();
            if (giornoChiusuraDate.equals(dataConsegna.toLocalDate())) {
                throw new IllegalArgumentException("La data selezionata è un giorno di chiusura eccezionale.");
            }
        }

        LocalDate dataSoloData = dataConsegna.toLocalDate();
        LocalDateTime apertura = LocalDateTime.of(dataSoloData, orarioApertura);
        LocalDateTime chiusura = LocalDateTime.of(dataSoloData, orarioChiusura);

        if (dataConsegna.isBefore(apertura) || dataConsegna.isAfter(chiusura)) {
            throw new IllegalArgumentException("La fascia oraria selezionata è fuori dall'orario di apertura.");
        }
        


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
            if (prodotto == null || prodotto.getAttivo()==false) {
                throw new IllegalArgumentException("Prodotto " + item.getString("name") + " non trovato.");
            }

            BigDecimal priceFromCart = item.getBigDecimal("price");
            BigDecimal priceFromDb = prodotto.getCosto();


            if (priceFromDb.compareTo(priceFromCart) != 0) {
                throw new IllegalArgumentException("Il prezzo del prodotto " + item.getString("name") + " non è valido.");
            }

            EItemOrdine itemOrdine = new EItemOrdine();
            itemOrdine.setOrdine(ordine);
            itemOrdine.setProdotto(prodotto);
            itemOrdine.setQuantita(qty);
            itemOrdine.setPrezzoUnitario(priceFromCart);

            ordine.addItemOrdine(itemOrdine);
            itemOrdineList.add(itemOrdine);

            totalPrice += priceFromCart.doubleValue() * qty;
        }

        ordine.setDataEsecuzione(LocalDateTime.now());
        ordine.setDataRicezione(LocalDateTime.now());
        ordine.setCosto(BigDecimal.valueOf(totalPrice));
        ordine.setCliente(cliente);
        ordine.setStato("in_attesa");
        ordine.setNote(note);
        ordine.setIndirizzoConsegna(indirizzoConsegna);
        ordine.setCartaPagamento(metodoPagamento);


        transaction = em.getTransaction();
        transaction.begin();


        em.persist(ordine);
        for (EItemOrdine itemOrdine : itemOrdineList) {

            em.persist(itemOrdine);
        }

        em.flush();
        transaction.commit();


        Template template = cfg.getTemplate("confirmed_order.ftl");
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("role", role);
        data.put("logged", logged);
        template.process(data, response.getWriter());

    } catch (IllegalArgumentException e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("errorMessage", e.getMessage());
        data.put("role", role);
        data.put("logged", logged);
        TemplateRenderer.mostraErrore(request, response, "order_error.ftl", e.getMessage(), role, logged);

    } catch (Exception e) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }

            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
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

        OrderTimeCalculator orderTime = new OrderTimeCalculator(em);
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
