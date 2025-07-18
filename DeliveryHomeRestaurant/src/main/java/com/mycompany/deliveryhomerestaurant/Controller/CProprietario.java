/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;


import com.mycompany.deliveryhomerestaurant.DAO.ECalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECategoriaDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECuocoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EExceptionCalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ERiderDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ESegnalazioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECategoriaDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECuocoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EExceptionCalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EProdottoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERiderDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ESegnalazioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECalendario;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import com.mycompany.deliveryhomerestaurant.Model.EExceptionCalendario;
import com.mycompany.deliveryhomerestaurant.Model.EItemOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.EProprietario;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;
import com.mycompany.deliveryhomerestaurant.util.AccessControlUtil;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
import com.mycompany.deliveryhomerestaurant.util.UtilFlashMessages;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
        boolean logged = true; 

        try {

            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();



            EOrdineDao ordineDao = new EOrdineDAOImpl(em);
            List<EOrdine> allOrders = ordineDao.getAllOrders();

            allOrders.sort((a, b) -> b.getDataEsecuzione().compareTo(a.getDataEsecuzione()));

            List<EOrdine> recentOrders = allOrders.stream().limit(5).collect(Collectors.toList());


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

            TemplateRenderer.render(request, response, "admin_panel.ftl", data);

        } catch(SecurityException e){
            
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
            
        }  catch (Exception e) {
            
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void showReviews(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        boolean logged = true;

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();


            // filtro/sort dalla query string
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";
            String stars = request.getParameter("stars") != null ? request.getParameter("stars") : "all";
            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            
            ERecensioneDAO recensioneDao = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDao.getAllReviews();

            if (!stars.equals("all")) {
                int starsInt = Integer.parseInt(stars);
                allReviews = allReviews.stream()
                    .filter(r -> r.getVoto() == starsInt)
                    .collect(Collectors.toList());
            }


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
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "recensioni_admin.ftl", data);

        } catch(SecurityException e){
				if(session != null && session.getAttribute("utente") != null){
					EUtente utente =  (EUtente) session.getAttribute("utente");
					role = utente.getRuolo();
				}
				TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
				
        }  catch (Exception e) {
            
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
            
        }
    }
    
    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
           
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();
            

 


            EOrdineDao ordineDao = new EOrdineDAOImpl(em);
            List<EOrdine> allOrders = ordineDao.getAllOrders();

            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String status = request.getParameter("status") != null ? request.getParameter("status") : "all";
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";


            if (!status.equals("all")) {
                allOrders = allOrders.stream()
                    .filter(o -> status.equals(o.getStato()))
                    .collect(Collectors.toList());
            }


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
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "admin_order.ftl", data);

        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void showSegnalazioni(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();



            ESegnalazioneDAO segnalazioneDao = new ESegnalazioneDAOImpl(em);
            List<ESegnalazione> segnalazioni = segnalazioneDao.getAllWarnings(); // o getAllSegnalazioni()


            String search = request.getParameter("search") != null ? request.getParameter("search") : "";
            String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "newest";


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


            if ("newest".equals(sort)) {
                segnalazioni.sort((a, b) -> b.getData().compareTo(a.getData()));
            } else if ("oldest".equals(sort)) {
                segnalazioni.sort((a, b) -> a.getData().compareTo(b.getData()));
            }


            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("segnalazioni", segnalazioni);
            data.put("logged", logged);
            data.put("role", role);
            data.put("search", search);
            data.put("sort", sort);
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "admin_segnalazioni.ftl", data);


        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        }  catch (Exception e) {
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }

    public void showDashboard(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();



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
            List<EProdotto> prodotti = prodottoDao.getAllActiveProducts();
            Map<Integer, String> mappaProdotti = new HashMap<>();
            for (EProdotto prodotto : prodotti) {
                mappaProdotti.put(prodotto.getId(), prodotto.getNome());
            }

            Map<Integer, Integer> conteggioPiatti = new HashMap<>();
            for (EOrdine ordine : allOrders) {
                List<EItemOrdine> itemOrdine = ordine.getItemOrdini();
                for (EItemOrdine i : itemOrdine) {
                    int prodottoId = i.getProdotto().getId();
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


            EClienteDAO clienteDao = new EClienteDAOImpl(em);
            List<?> clienti = clienteDao.getAllClients();
            int numeroClienti = clienti.size();

    
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

            TemplateRenderer.render(request, response, "dashboard.ftl", data);

        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void showMenu(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();

            EProdottoDAO prodottoDao = new EProdottoDAOImpl(em);
            List<EProdotto> prodotti = prodottoDao.getAllActiveProducts();
            
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
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "menu_admin.ftl", data);

        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void saveProduct(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        EProdottoDAOImpl prodottoDao = new EProdottoDAOImpl(em);
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

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
                List<EProdotto> prodottiNonAttivi = prodottoDao.getAllInactiveProducts();
                prodotto = prodottiNonAttivi.stream()
                    .filter(p -> p.getNome().equals(nome))
                    .findFirst()
                    .orElse(new EProdotto());
            }

            prodotto.setNome(nome);
            prodotto.setCategoria(categoria);
            prodotto.setDescrizione(descrizione);
            prodotto.setCosto(costo);
            prodotto.setAttivo(true);

            em.getTransaction().begin();
            em.persist(prodotto);
            em.getTransaction().commit();
            UtilFlashMessages.addMessage(request, "success", "Prodotto aggiunto con successo");
            response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");

        } catch (Exception e) {
            
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
     
    public void modifyProduct(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

            EntityManager em = (EntityManager) request.getAttribute("em");
            HttpSession session = UtilSession.getSession(request);
            boolean logged = true;
            String role = "";

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
                UtilFlashMessages.addMessage(request, "success", "Prodotto modificato con successo");
                response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");

            } catch (Exception e) {
                
                            
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
            }
        }
    
    public void deleteProduct(HttpServletRequest request, HttpServletResponse response, String[] params)
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
                Long id = Long.parseLong(idStr);
                EProdotto prodotto = em.find(EProdotto.class, id);
                prodotto.setAttivo(false);
                em.getTransaction().begin();
                em.persist(prodotto);
                em.getTransaction().commit();
                UtilFlashMessages.addMessage(request, "success", "Prodotto eliminato con successo!");
                response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");
            } catch(Exception e){
                if(em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                UtilFlashMessages.addMessage(request, "error", e.getMessage());
                response.sendRedirect(request.getContextPath() + "/Proprietario/showMenu");
            }
    }
    
    public void showCreateAccount(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();



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
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "create_account_admin.ftl", data);

        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
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
            UtilFlashMessages.addMessage(request, "success", "Account dipendente creato con successo");
        } else {
            UtilFlashMessages.addMessage(request, "error", "Errore nella creazione dell'account");
        }
        response.sendRedirect(request.getContextPath() + "/Proprietario/showCreateAccount");
    }

    public void showCalendar(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            EProprietario proprietario = AccessControlUtil.checkUserRole(utente, EProprietario.class);
            role = proprietario.getRuolo();


            ECalendarioDAO calendarioDAO = new ECalendarioDAOImpl(em);
            EExceptionCalendarioDAO exceptioncalendarioDAO = new EExceptionCalendarioDAOImpl(em);
            List<ECalendario> giorniChiusuraSettimanali = calendarioDAO.getCalendario();
            List<EExceptionCalendario> giorniChiusuraEccezionali = exceptioncalendarioDAO.getExceptionCalendario();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter dbDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            // trasformo la lista in una lista di Mappe con stringhe già formattate
            List<Map<String, Object>> giorniFormattati = new ArrayList<>();
            for (ECalendario giorno : giorniChiusuraSettimanali) {
                Map<String, Object> map = new HashMap<>();
                map.put("data", giorno.getData());
                map.put("orarioApertura", giorno.getOrarioApertura() != null ? giorno.getOrarioApertura().format(timeFormatter) : null);
                map.put("orarioChiusura", giorno.getOrarioChiusura() != null ? giorno.getOrarioChiusura().format(timeFormatter) : null);
                map.put("aperto", giorno.isAperto());
                giorniFormattati.add(map);
            }
            
            // mappatura giorniChiusuraEccezionali
            List<Map<String, Object>> eccezioniFormattate = new ArrayList<>();
            for (EExceptionCalendario eccezione : giorniChiusuraEccezionali) {
                Map<String, Object> map = new HashMap<>();
                map.put("exceptionDate", eccezione.getExceptionDate().format(dateFormatter));
                map.put("exceptionDateRaw", eccezione.getExceptionDate().format(dbDateFormatter)); // per <input hidden>
                map.put("orarioApertura", eccezione.getOrarioApertura() != null ? eccezione.getOrarioApertura().format(timeFormatter) : "");
                map.put("orarioChiusura", eccezione.getOrarioChiusura() != null ? eccezione.getOrarioChiusura().format(timeFormatter) : "");
                map.put("aperto", eccezione.isAperto());
                map.put("motivoChiusura", eccezione.getMotivoChiusura());
                eccezioniFormattate.add(map);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("logged", logged);
            data.put("role", role);
            data.put("giorniChiusuraSettimanali", giorniFormattati);
            data.put("giorniChiusuraEccezionali", eccezioniFormattate);
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("messages", messages);

            TemplateRenderer.render(request, response, "calendar_admin.ftl", data);
        } catch(SecurityException e){
	if(session != null && session.getAttribute("utente") != null){
		EUtente utente =  (EUtente) session.getAttribute("utente");
		role = utente.getRuolo();
	}
	TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void addExceptionDay(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

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

            String dataStr = request.getParameter("dataChiusura");

            if (dataStr == null || dataStr.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data mancante");
                return;
            }

            LocalDate dataChiusura;
            try {
                dataChiusura = LocalDate.parse(dataStr); // formato yyyy-MM-dd
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data non valido");
                return;
            }

            EExceptionCalendario eccezione = new EExceptionCalendario();
            eccezione.setExceptionDate(dataChiusura);
            eccezione.setAperto(false); // giorno chiuso

            em.getTransaction().begin();
            em.persist(eccezione);
            em.getTransaction().commit();
            UtilFlashMessages.addMessage(request, "success", "Operazione eseguita con successo");
            response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");

        } catch (Exception e) {
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }

    public void deleteExceptionDay(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        boolean logged = true;
        String role = "";

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

            String dataChiusuraStr = request.getParameter("dataChiusura");
            if (dataChiusuraStr == null || dataChiusuraStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data mancante");
                return;
            }

            LocalDate dataChiusura;
            try {
                dataChiusura = LocalDate.parse(dataChiusuraStr);
            } catch (DateTimeParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato data non valido");
                return;
            }

            EExceptionCalendario eccezione = null;
            try {
                eccezione = em.createQuery(
                        "SELECT e FROM EExceptionCalendario e WHERE e.exceptionDate = :date", EExceptionCalendario.class)
                        .setParameter("date", dataChiusura)
                        .getSingleResult();
            } catch (NoResultException e) {
                
            }

            if (eccezione == null) {
                UtilFlashMessages.addMessage(request, "error", "Giorno di chiusura eccezionale non trovato");
            } else {

                em.getTransaction().begin();
                EExceptionCalendario toRemove = em.find(EExceptionCalendario.class, eccezione.getId());
                if (toRemove != null) {
                    em.remove(toRemove);
                    em.getTransaction().commit();
                    UtilFlashMessages.addMessage(request, "success", "Giorno di chiusura eccezionale rimosso con successo");
                } else {
                    em.getTransaction().rollback();
                    UtilFlashMessages.addMessage(request, "error", "Errore durante la rimozione del giorno di chiusura eccezionale");
                }
            }
            response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");

        } catch (Exception e) {
            
                        
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void editDay(HttpServletRequest request, HttpServletResponse response, String[] params)
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

        
            String giornoStr = request.getParameter("giorno"); 
            String aperturaStr = request.getParameter("orariapertura");
            String chiusuraStr = request.getParameter("orarichiusura");
            String stato = request.getParameter("orari[stato]");

            if (giornoStr == null || stato == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati mancanti");
                return;
            }

            DayOfWeek day;
            try {
                day = DayOfWeek.valueOf(giornoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giorno della settimana non valido");
                return;
            }

            ECalendario giornoCalendario = em.find(ECalendario.class, day);
            if (giornoCalendario == null) {
                UtilFlashMessages.addMessage(request, "error", "Giorno calendario non trovato");
                response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");
                return;
            }

            em.getTransaction().begin();

            if ("chiuso".equalsIgnoreCase(stato)) {
                giornoCalendario.setAperto(false);
                giornoCalendario.setOrarioApertura(null);
                giornoCalendario.setOrarioChiusura(null);
            } else {
                giornoCalendario.setAperto(true);

                LocalTime orarioApertura = null;
                LocalTime orarioChiusura = null;

                try {
                    orarioApertura = (aperturaStr != null && !aperturaStr.isEmpty()) ? LocalTime.parse(aperturaStr) : null;
                    orarioChiusura = (chiusuraStr != null && !chiusuraStr.isEmpty()) ? LocalTime.parse(chiusuraStr) : null;
                } catch (DateTimeParseException e) {
                    em.getTransaction().rollback();
                    UtilFlashMessages.addMessage(request, "error", "Formato orario non valido");
                    response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");
                    return;
                }

                if (orarioApertura == null || orarioChiusura == null || !orarioApertura.isBefore(orarioChiusura)) {
                    em.getTransaction().rollback();
                    UtilFlashMessages.addMessage(request, "error", "L'orario di apertura deve essere precedente all'orario di chiusura");
                    response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");
                    return;
                }

                giornoCalendario.setOrarioApertura(orarioApertura);
                giornoCalendario.setOrarioChiusura(orarioChiusura);
            }

            em.merge(giornoCalendario);
            em.getTransaction().commit();

            UtilFlashMessages.addMessage(request, "success", "Giorno modificato con successo");
            response.sendRedirect(request.getContextPath() + "/Proprietario/showCalendar");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ServletException("Errore durante la modifica del giorno", e);
        }
    }

}
