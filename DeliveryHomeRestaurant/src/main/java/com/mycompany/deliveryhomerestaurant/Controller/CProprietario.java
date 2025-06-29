/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;


import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ESegnalazioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ESegnalazioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EProdottoDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EProprietario;
import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 * @author simone
 */
public class CProprietario {
    
    public void showPanel(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        String role = "";

        try {
            // Controllo ruolo
            EProprietario proprietario = null;
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                Object utente = session.getAttribute("utente");
                if (utente instanceof EProprietario) {
                    proprietario = (EProprietario) utente;
                    logged = true;
                    role = proprietario.getRuolo();
                }
            }

            if (proprietario == null) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            // ordini
            EOrdineDao ordineDao = new EOrdineDAOImpl(em);
            List<EOrdine> allOrders = ordineDao.getAllOrders();

            allOrders.sort((a, b) -> b.getDataEsecuzione().compareTo(a.getDataEsecuzione()));

            List<EOrdine> recentOrders = allOrders.stream().limit(5).collect(Collectors.toList());

            // settimana corrente
            LocalDateTime oggi = LocalDateTime.now();
            LocalDateTime inizioSettimana = oggi.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
            LocalDateTime fineSettimana = inizioSettimana.plusDays(6).with(LocalTime.MAX);

            int ordiniSettimana = 0;
            BigDecimal totaleSettimana = BigDecimal.ZERO;

            for (EOrdine ordine : allOrders) {
                LocalDateTime dataOrdine = ordine.getDataEsecuzione();
                if (dataOrdine.isAfter(inizioSettimana.minusSeconds(1)) && dataOrdine.isBefore(fineSettimana.plusSeconds(1))) {
                    ordiniSettimana++;
                    totaleSettimana = totaleSettimana.add(ordine.getCosto());
                }
            }

            // clienti
            EClienteDAO clienteDao = new EClienteDAOImpl(em);
            int numeroClienti = clienteDao.getAllClients().size();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", recentOrders);
            data.put("ordiniSettimana", ordiniSettimana);
            data.put("totaleSettimana", totaleSettimana);
            data.put("numeroClienti", numeroClienti);
            data.put("logged", logged);
            data.put("role", role);

            Template template = cfg.getTemplate("admin_panel.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel caricamento del pannello proprietario", e);
        }
    }
    
    public void showReviews(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        String role = "";

        try {
            EProprietario proprietario = null;
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                Object utente = session.getAttribute("utente");
                if (utente instanceof EProprietario) {
                    proprietario = (EProprietario) utente;
                    logged = true;
                    role = proprietario.getRuolo();
                }
            }

            if (proprietario == null) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            // filtro/sort dalla query string
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";
            String stars = request.getParameter("stars") != null ? request.getParameter("stars") : "all";
            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            
            // recensioni
            ERecensioneDAO recensioneDao = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDao.getAllReviews();

            // stelle
            if (!stars.equals("all")) {
                int starsInt = Integer.parseInt(stars);
                allReviews = allReviews.stream()
                    .filter(r -> r.getVoto() == starsInt)
                    .collect(Collectors.toList());
            }

            // testo
            if (!search.isEmpty()) {
                String searchLower = search.toLowerCase();
                allReviews = allReviews.stream()
                    .filter(r -> {
                        String descrizione = r.getDescrizione() != null ? r.getDescrizione().toLowerCase() : "";
                        String nome = r.getCliente() != null && r.getCliente().getNome() != null ? r.getCliente().getNome().toLowerCase() : "";
                        String cognome = r.getCliente() != null && r.getCliente().getCognome() != null ? r.getCliente().getCognome().toLowerCase() : "";
                        return descrizione.contains(searchLower) || nome.contains(searchLower) || cognome.contains(searchLower);
                    })
                    .collect(Collectors.toList());
            }

            if (sort.equals("newest")) {
                allReviews.sort((a, b) -> b.getData().compareTo(a.getData()));
            } else if (sort.equals("oldest")) {
                allReviews.sort((a, b) -> a.getData().compareTo(b.getData()));
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("reviews", allReviews);
            data.put("logged", logged);
            data.put("role", role);
            data.put("search", search);
            data.put("stars", stars);
            data.put("sort", sort);

            Template template = cfg.getTemplate("recensioni_admin.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione delle recensioni", e);
        }
    }
    
    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);

        try {
            EProprietario proprietario = null;
            boolean logged = false;
            String role = "";

            if (session != null && session.getAttribute("utente") != null) {
                Object utente = session.getAttribute("utente");
                if (utente instanceof EProprietario) {
                    proprietario = (EProprietario) utente;
                    logged = true;
                    role = proprietario.getRuolo();
                }
            }

            if (proprietario == null) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            // ordinazioni
            EOrdineDao ordineDao = new EOrdineDAOImpl(em);
            List<EOrdine> allOrders = ordineDao.getAllOrders();

            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String status = request.getParameter("status") != null ? request.getParameter("status") : "all";
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";

            // stato
            if (!status.equals("all")) {
                allOrders = allOrders.stream()
                    .filter(o -> status.equals(o.getStato()))
                    .collect(Collectors.toList());
            }

            // testo 
            if (!search.isEmpty()) {
                String searchLower = search.toLowerCase();
                allOrders = allOrders.stream()
                    .filter(o -> {
                        String nome = o.getCliente() != null && o.getCliente().getNome() != null ? o.getCliente().getNome().toLowerCase() : "";
                        String cognome = o.getCliente() != null && o.getCliente().getCognome() != null ? o.getCliente().getCognome().toLowerCase() : "";
                        String id = String.valueOf(o.getId());

                        return nome.contains(searchLower) || cognome.contains(searchLower) || id.contains(searchLower);
                    })
                    .collect(Collectors.toList());
            }

            // data esecuzione
            if ("newest".equals(sort)) {
                allOrders.sort((a, b) -> b.getDataEsecuzione().compareTo(a.getDataEsecuzione()));
            } else {
                allOrders.sort((a, b) -> a.getDataEsecuzione().compareTo(b.getDataEsecuzione()));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", allOrders);
            data.put("logged", logged);
            data.put("role", role);
            data.put("search", search);
            data.put("status", status);
            data.put("sort", sort);

            Template template = cfg.getTemplate("admin_order.ftl");  
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione degli ordini", e);
        }
    }
    
    public void showSegnalazioni(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);

        try {
            boolean logged = false;
            String role = "";
            EProprietario proprietario = null;

            if (session != null && session.getAttribute("utente") instanceof EProprietario) {
                proprietario = (EProprietario) session.getAttribute("utente");
                logged = true;
                role = proprietario.getRuolo();
            }

            if (proprietario == null) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            // segnalazioni
            ESegnalazioneDAO segnalazioneDao = new ESegnalazioneDAOImpl(em);
            List<ESegnalazione> segnalazioni = segnalazioneDao.getAllWarnings(); // o getAllSegnalazioni()

            // Parametri GET
            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";

            // Filtro testo
            if (!search.isEmpty()) {
                String searchLower = search.toLowerCase();

                segnalazioni = segnalazioni.stream()
                    .filter(s -> {
                        String descrizione = s.getDescrizione() != null ? s.getDescrizione().toLowerCase() : "";
                        String nome = "", cognome = "";

                        if (s.getOrdine() != null && s.getOrdine().getCliente() != null) {
                            nome = s.getOrdine().getCliente().getNome() != null ? s.getOrdine().getCliente().getNome().toLowerCase() : "";
                            cognome = s.getOrdine().getCliente().getCognome() != null ? s.getOrdine().getCliente().getCognome().toLowerCase() : "";
                        }

                        return descrizione.contains(searchLower)
                                || nome.contains(searchLower)
                                || cognome.contains(searchLower);
                    })
                    .collect(Collectors.toList());
            }

            // Ordinamento
            if ("newest".equals(sort)) {
                segnalazioni.sort((a, b) -> b.getData().compareTo(a.getData()));
            } else if ("oldest".equals(sort)) {
                segnalazioni.sort((a, b) -> a.getData().compareTo(b.getData()));
            }

            // Output alla view
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("segnalazioni", segnalazioni);
            data.put("logged", logged);
            data.put("role", role);
            data.put("search", search);
            data.put("sort", sort);

            Template template = cfg.getTemplate("admin_segnalazioni.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione delle segnalazioni", e);
        }
    }

    public void showDashboard(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);

        try {
            EProprietario proprietario = null;
            boolean logged = false;
            String role = "";

            if (session != null && session.getAttribute("utente") instanceof EProprietario) {
                proprietario = (EProprietario) session.getAttribute("utente");
                logged = true;
                role = proprietario.getRuolo();
            }

            if (proprietario == null) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            EOrdineDao ordineDao = new EOrdineDAOImpl(em);
            List<EOrdine> allOrders = ordineDao.getAllOrders();

            allOrders.sort((a, b) -> b.getDataEsecuzione().compareTo(a.getDataEsecuzione()));

            List<EOrdine> recentOrders = allOrders.stream().limit(5).collect(Collectors.toList());

            LocalDateTime now = LocalDateTime.now();
            LocalDate oggi = now.toLocalDate();
            LocalDateTime inizioOggi = oggi.atStartOfDay();
            LocalDateTime fineOggi = oggi.atTime(LocalTime.MAX);

            LocalDate inizioSettimanaDate = oggi.with(DayOfWeek.MONDAY);
            LocalDateTime inizioSettimana = inizioSettimanaDate.atStartOfDay();
            LocalDateTime fineSettimana = inizioSettimanaDate.plusDays(6).atTime(LocalTime.MAX);

            BigDecimal totaleOggi = BigDecimal.ZERO;
            int ordiniOggi = 0;

            for (EOrdine ordine : allOrders) {
                LocalDateTime dataOrdine = ordine.getDataEsecuzione();
                if (!dataOrdine.isBefore(inizioOggi) && !dataOrdine.isAfter(fineOggi)) {
                    totaleOggi = totaleOggi.add(ordine.getCosto());
                    ordiniOggi++;
                }
            }

            EProdottoDAO prodottoDao = new EProdottoDAOImpl(em);
            List<EProdotto> prodotti = prodottoDao.getAllProducts();
            Map<Integer, String> mappaProdotti = new HashMap<>();
            for (EProdotto prodotto : prodotti) {
                mappaProdotti.put(prodotto.getId(), prodotto.getNome());
            }

            Map<Integer, Integer> conteggioPiatti = new HashMap<>();
            for (EOrdine ordine : allOrders) {
                Set<EProdotto> prodottiOrdine = ordine.getProdotti();
                for (EProdotto p : prodottiOrdine) {
                    int prodottoId = p.getId();
                    conteggioPiatti.put(prodottoId, conteggioPiatti.getOrDefault(prodottoId, 0) + 1);
                }
            }


            LinkedHashMap<Integer, Integer> top10Piatti = conteggioPiatti.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            List<String> nomiTopPiatti = new ArrayList<>();
            List<Integer> quantitaTopPiatti = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : top10Piatti.entrySet()) {
                String nome = mappaProdotti.getOrDefault(entry.getKey(), "Sconosciuto");
                nomiTopPiatti.add(nome);
                quantitaTopPiatti.add(entry.getValue());
            }

            // clienti
            EClienteDAO clienteDao = new EClienteDAOImpl(em);
            List<?> clienti = clienteDao.getAllClients();
            int numeroClienti = clienti.size();

            // recensioni
            ERecensioneDAO recensioneDao = new ERecensioneDAOImpl(em);
            List<ERecensione> recensioni = recensioneDao.getAllReviews();

            double sommaValutazioni = 0;
            int numeroRecensioni = recensioni.size();

            for (ERecensione rec : recensioni) {
                sommaValutazioni += rec.getVoto();
            }

            double mediaValutazioni = numeroRecensioni > 0 ? sommaValutazioni / numeroRecensioni : 0;

            Map<LocalDate, Double> fatturatoSettimana = new LinkedHashMap<>();
            for (int i = 0; i < 7; i++) {
                LocalDate data = inizioSettimanaDate.plusDays(i);
                fatturatoSettimana.put(data, 0.0);
            }

            for (EOrdine ordine : allOrders) {
                LocalDate dataOrdine = ordine.getDataEsecuzione().toLocalDate();
                if (fatturatoSettimana.containsKey(dataOrdine)) {
                    double attuale = fatturatoSettimana.get(dataOrdine);
                    attuale += ordine.getCosto().doubleValue();
                    fatturatoSettimana.put(dataOrdine, attuale);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", recentOrders);
            data.put("totaleOggi", totaleOggi);
            data.put("numeroClienti", numeroClienti);
            data.put("ordiniOggi", ordiniOggi);
            data.put("mediaValutazioni", mediaValutazioni);
            data.put("fatturatoSettimana", new ArrayList<>(fatturatoSettimana.values()));
            data.put("nomiTopPiatti", nomiTopPiatti);
            data.put("quantitaTopPiatti", quantitaTopPiatti);
            data.put("logged", logged);
            data.put("role", role);

            Template template = cfg.getTemplate("dashboard.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione della dashboard", e);
        }
    }



    
}
