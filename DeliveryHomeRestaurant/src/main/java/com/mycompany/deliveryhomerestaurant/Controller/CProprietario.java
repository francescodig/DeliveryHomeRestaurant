/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;


import com.mycompany.deliveryhomerestaurant.DAO.ECategoriaDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECuocoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ESegnalazioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ERiderDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECategoriaDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECuocoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ESegnalazioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EProdottoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERiderDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EProprietario;
import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;
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
import java.util.UUID;



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
    
    public void showMenu(HttpServletRequest request, HttpServletResponse response, String[] params)
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

            EProdottoDAO prodottoDao = new EProdottoDAOImpl(em);
            List<EProdotto> prodotti = prodottoDao.getAllProducts();
            
            ECategoriaDAO categoriaDao = new ECategoriaDAOImpl(em);
            List<ECategoria> categorie = categoriaDao.getAllCategories();

            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String categoryFilter = request.getParameter("category") != null ? request.getParameter("category") : "all";

            if (!"all".equalsIgnoreCase(categoryFilter)) {
                prodotti = prodotti.stream()
                    .filter(p -> p.getCategoria() != null
                              && categoryFilter.equalsIgnoreCase(p.getCategoria().getNome()))
                    .collect(Collectors.toList());
            }

            if (!search.isEmpty()) {
                String searchLower = search.toLowerCase();
                prodotti = prodotti.stream()
                    .filter(p -> (p.getNome() != null && p.getNome().toLowerCase().contains(searchLower))
                              || (p.getDescrizione() != null && p.getDescrizione().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("prodotti", prodotti);
            data.put("logged", logged);
            data.put("role", role);
            data.put("search", search);
            data.put("category", categoryFilter);
            data.put("categorie", categorie);

            Template template = cfg.getTemplate("menu_admin.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione del menu", e);
        }
    }
    
    public void saveProduct(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);

        try {
            Object utente = session != null ? session.getAttribute("utente") : null;
            if (!(utente instanceof EProprietario)) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            EProprietario proprietario = (EProprietario) utente;
            if (!"proprietario".equalsIgnoreCase(proprietario.getRuolo())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato");
                return;
            }

            String idStr = request.getParameter("product_id");
            String nome = request.getParameter("nome") != null ? request.getParameter("nome").trim() : "";
            String categoriaIdStr = request.getParameter("categoria_id");
            String descrizione = request.getParameter("descrizione") != null ? request.getParameter("descrizione").trim() : "";
            String costoStr = request.getParameter("costo");

            if (nome.isEmpty() || categoriaIdStr == null || categoriaIdStr.isEmpty() || descrizione.isEmpty() || costoStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati mancanti o non validi");
                return;
            }

            BigDecimal costo;
            try {
                costo = new BigDecimal(costoStr);
                if (costo.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Costo non valido");
                return;
            }

            Long categoriaId;
            ECategoria categoria;
            try {
                categoriaId = Long.parseLong(categoriaIdStr);
                categoria = em.find(ECategoria.class, categoriaId);
                if (categoria == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Categoria non trovata");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID categoria non valido");
                return;
            }

            EProdotto prodotto;
            if (idStr != null && !idStr.isEmpty()) {
                Long id = Long.parseLong(idStr);
                prodotto = em.find(EProdotto.class, id);
                if (prodotto == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
                    return;
                }
            } else {
                prodotto = new EProdotto();
            }

            prodotto.setNome(nome);
            prodotto.setCategoria(categoria);
            prodotto.setDescrizione(descrizione);
            prodotto.setCosto(costo);

            em.getTransaction().begin();
            em.persist(prodotto);
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");

        } catch (Exception e) {
            throw new ServletException("Errore durante il salvataggio del prodotto", e);
        }
    }
     
    public void modifyProduct(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException {

            EntityManager em = (EntityManager) request.getAttribute("em");
            HttpSession session = UtilSession.getSession(request);

            try {
                Object utente = session != null ? session.getAttribute("utente") : null;
                if (!(utente instanceof EProprietario)) {
                    response.sendRedirect(request.getContextPath() + "/showLogin");
                    return;
                }

                EProprietario proprietario = (EProprietario) utente;
                if (!"proprietario".equalsIgnoreCase(proprietario.getRuolo())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato");
                    return;
                }

                String idStr = request.getParameter("product_id");
                String nome = request.getParameter("nome") != null ? request.getParameter("nome").trim() : "";
                String categoriaIdStr = request.getParameter("categoria_id");
                String descrizione = request.getParameter("descrizione") != null ? request.getParameter("descrizione").trim() : "";
                String costoStr = request.getParameter("costo");

                // Validazione base dei dati
                if (nome.isEmpty() || categoriaIdStr == null || categoriaIdStr.isEmpty() || descrizione.isEmpty() || costoStr == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati mancanti o non validi");
                    return;
                }

                BigDecimal costo;
                try {
                    costo = new BigDecimal(costoStr);
                    if (costo.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Costo non valido");
                    return;
                }

                Long categoriaId;
                ECategoria categoria;
                try {
                    categoriaId = Long.parseLong(categoriaIdStr);
                    categoria = em.find(ECategoria.class, categoriaId);
                    if (categoria == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Categoria non valida");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID categoria non valido");
                    return;
                }

                EProdotto prodotto;
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        Long id = Long.parseLong(idStr);
                        prodotto = em.find(EProdotto.class, id);
                        if (prodotto == null) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID prodotto non valido");
                        return;
                    }
                } else {
                    prodotto = new EProdotto();
                }

                prodotto.setNome(nome);
                prodotto.setCategoria(categoria);
                prodotto.setDescrizione(descrizione);
                prodotto.setCosto(costo);

                em.getTransaction().begin();
                em.persist(prodotto);
                em.getTransaction().commit();

                response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");

            } catch (Exception e) {
                throw new ServletException("Errore durante la modifica del prodotto", e);
            }
        }
    
    //deleteProduct da fare
    
    public void showCreateAccount(HttpServletRequest request, HttpServletResponse response, String[] params)
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

            if (proprietario == null || !"proprietario".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/showLogin");
                return;
            }

            ECuocoDAO chefDao = new ECuocoDAOImpl(em);
            List<ECuoco> chefs = chefDao.getAllChefs();

            ERiderDAO riderDao = new ERiderDAOImpl(em);
            List<ERider> riders = riderDao.getAllRiders();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("logged", logged);
            data.put("role", role);
            data.put("chefs", chefs);
            data.put("riders", riders);

            Template template = cfg.getTemplate("create_account_admin.ftl");
            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nella visualizzazione della pagina di creazione account", e);
        }
    }
    
    public void createEmployee(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String ruolo = request.getParameter("ruolo");

        EntityManager em = (EntityManager) request.getAttribute("em");
        EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
        ProfiloService service = new ProfiloServiceImpl(utenteDAO);

        EUtente<?> nuovoUtente;

        if ("Cuoco".equalsIgnoreCase(ruolo)) {
            ECuoco cuoco = new ECuoco();
            String codiceCuoco = "CUOCO-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            cuoco.setCodiceCuoco(codiceCuoco);
            nuovoUtente = cuoco;
        } else if ("Rider".equalsIgnoreCase(ruolo)) {
            ERider rider = new ERider();
            String codiceRider = "RIDER-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            rider.setCodiceRider(codiceRider);
            nuovoUtente = rider;
        } else {
            request.setAttribute("error", "Ruolo non valido.");
            request.getRequestDispatcher("/WEB-INF/views/create_account_admin.ftl").forward(request, response);
            return;
        }

        nuovoUtente.setNome(nome)
                   .setCognome(cognome)
                   .setEmail(email)
                   .setPassword(password);

        boolean success = service.Register(nuovoUtente);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/Proprietario/showCreateAccount");
        } else {
            request.setAttribute("error", "Creazione dipendente fallita.");
            request.getRequestDispatcher("/WEB-INF/views/create_account_admin.ftl").forward(request, response);
        }
    }


    //delete Employee da fare eventualmente


}
