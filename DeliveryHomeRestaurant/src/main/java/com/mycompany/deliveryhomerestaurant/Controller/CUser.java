package com.mycompany.deliveryhomerestaurant.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECartaCreditoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EIndirizzoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EMenuDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.MailServiceImpl;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;
import com.mycompany.deliveryhomerestaurant.util.AccessControlUtil;
import com.mycompany.deliveryhomerestaurant.util.InputSanitizer;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
import com.mycompany.deliveryhomerestaurant.util.UtilFlashMessages;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CUser{


    public void home(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        String role = "";
        boolean logged = true;
        HttpSession session = request.getSession();
        
        try {
           



            ERecensioneDAO recensioneDAO = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDAO.getAllReviews();
            Collections.shuffle(allReviews);
            List<ERecensione> reviews = allReviews.subList(0, Math.min(3, allReviews.size()));

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("reviews", reviews);
            
            logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
                EUtente utente = (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            data.put("logged", logged);
            data.put("role", role);

            TemplateRenderer.render(request, response, "home.ftl", data);

        } catch (Exception e) {
            
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        } 
            
    }

    public void mostraMenu(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        boolean logged = false;
        
        try {
            

            EMenuDAO menuDAO = new EMenuDAOImpl(em);
            List<Map<String, Object>> menu = menuDAO.getMenu();
            
            if(session != null && session.getAttribute("utente")!=null){
                
                EUtente utente = (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
                logged = true;
                
            }
            
            

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("menu", menu);
            data.put("role", role);
            data.put("logged", logged);

            response.setContentType("text/html;charset=UTF-8");
            TemplateRenderer.render(request, response, "menu.ftl", data);

        } catch (Exception e) {
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
    
    public void getMenuJson(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException{
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        
        EMenuDAO menuDAO = new EMenuDAOImpl(em);
        
        List<Map<String, Object>>  menu =  menuDAO.getMenu();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        // Supponiamo che menu sia già una List<Map<String, Object>>
        String json = mapper.writeValueAsString(menu);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();

        
        
    }

    public void order(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
         EntityManager em = (EntityManager) request.getAttribute("em");
         Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
         String role = "";
         boolean logged = true; 
         HttpSession session = UtilSession.getSession(request);
         
        try {

          

            EMenuDAO menuDAO = new EMenuDAOImpl(em);
            List<Map<String, Object>> menu = menuDAO.getMenu();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("menu", menu);
            
            logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
                EUtente utente = (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
                if(!role.equals("cliente") && !role.equals(null)){
                    throw new SecurityException("Accesso negato: ruolo non autorizzato.");
                }
            }
            data.put("logged", logged);
            data.put("role", role);

            response.setContentType("text/html;charset=UTF-8");
            
            TemplateRenderer.render(request, response, "order.ftl", data);

        } catch(SecurityException e){
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
            
        }  catch (Exception e) {
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }

    public void showMyOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        boolean logged = true; 
        
        
        try {
            
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ECliente cliente = AccessControlUtil.checkUserRole(utente, ECliente.class);
            role = cliente.getRuolo();

  

     


            EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
            List<EOrdine> mieiOrdini = ordineDAO.getOrdersByClient(cliente);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", mieiOrdini);
            data.put("logged", logged);
            data.put("role",role);

            response.setContentType("text/html;charset=UTF-8");
           TemplateRenderer.render(request, response, "miei_ordini.ftl", data);

        } catch(SecurityException e){
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
            
        } 
        catch (Exception e) {
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        } 
    }

    public void showProfile(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        boolean logged = true;

        
        try {

            
            EUtente utente = null;
            EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
            logged = false;

            if (session != null && session.getAttribute("utente")!=null) {
                utente =  (EUtente) session.getAttribute("utente");
                EUtente attachedUser = utenteDAO.findById(utente.getId());
                logged = true;
                role = attachedUser.getRuolo();

               

                
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());
                Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
                data.put("messages", messages);
                                
                if(role.equals("cliente")){
                    ECliente client = (ECliente) session.getAttribute("utente");
                    ECliente clientAttached = (ECliente) utenteDAO.findById(client.getId()); 
                    List<EIndirizzo> indirizzi = clientAttached.getActiveIndirizziConsegna();
                    List<ECartaCredito> cards = clientAttached.getMetodiPagamento();
                    data.put("indirizzi", indirizzi);
                    data.put("carte_credito", cards);
                }
               
                data.put("role", utente.getRuolo());
                data.put("utente", utente);
                data.put("logged", logged);
                data.put("role", role);


                response.setContentType("text/html;charset=UTF-8");
                TemplateRenderer.render(request, response, "account.ftl", data);
            } else {
                logged = false;
                
                response.setContentType("text/html;charset=UTF-8");
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());
                data.put("role",role);

                TemplateRenderer.render(request, response, "login.ftl", data);
            }

        } catch (Exception e) {
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }  
    }
    
    
    public void showLoginForm(HttpServletRequest request, HttpServletResponse response, String[] params)
                throws ServletException, IOException, TemplateException {
        HttpSession session = UtilSession.startSession(request);
        boolean logged = false;
        String role = "";
        //Controllo se l'utente è gia loggato
        if(session.getAttribute("utente") != null){
            response.sendRedirect(request.getContextPath() + "/User/home/");
        }
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        try{
            
            Map<String, Object> data = new HashMap<>();
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("contextPath", request.getContextPath());
            data.put("messages", messages);
            data.put("role", "");
            TemplateRenderer.render(request, response, "login.ftl", data);
        }catch(Exception e){
            
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
            
        }
    }
    
    public void loginUser(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.startSession(request);
        boolean logged = false;
        String role = "";
        try {
            // se l'utente è gia in sessione 
            if(session.getAttribute("utente") != null){
                response.sendRedirect(request.getContextPath() + "/User/home/");
            }
            else{
                 // 1. Validazione parametri
                String email = InputSanitizer.sanitize(request.getParameter("username"));
                String password = InputSanitizer.sanitize(request.getParameter("password"));
                EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
                ProfiloService service = new ProfiloServiceImpl(utenteDAO);
                EUtente utenteLogin = service.login(email, password, session);
                if(utenteLogin != null) {
                    response.sendRedirect(request.getContextPath() + "/User/home/");
                    return;
                } else {
                    UtilFlashMessages.addMessage(request, "error", "Credenziali non valide");
                    response.sendRedirect(request.getContextPath() + "/User/showLoginForm/");
                }
            }
        }catch (Exception e) {
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/User/showLoginForm/");
        }
    }
    
    
    public void showRegisterForm(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        try{
            
            Map<String, Object> data = new HashMap<>();
            Map<String, List<String>> messages = UtilFlashMessages.getMessage(request);
            data.put("contextPath", request.getContextPath());
            data.put("messages", messages);
            data.put("role", "");
            TemplateRenderer.render(request, response, "register.ftl", data);
        }catch(Exception e){
            response.sendRedirect(request.getContextPath() + "/User/home/");
        }
  
        
    }
    
    public void registerUser(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException, MessagingException{
        
        String nome = InputSanitizer.sanitize(request.getParameter("nome"));
        String cognome = InputSanitizer.sanitize(request.getParameter("cognome"));
        String email = InputSanitizer.sanitize(request.getParameter("email"));
        String password = InputSanitizer.sanitize(request.getParameter("password"));
        String ruolo = InputSanitizer.sanitize(request.getParameter("role"));
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        

        
        ECliente utente = new ECliente ();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setPassword(password);
        EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
        ProfiloService service = new ProfiloServiceImpl(utenteDAO);
        
        boolean register = service.Register(utente);
        if(register){
            HttpSession session = UtilSession.startSession(request);
            EUtente utenteOnDb = utenteDAO.findByUsername(email);
            
            
            
            session.setAttribute("utente", utenteOnDb);
            
            
           
            MailServiceImpl emailServiceImpl = new MailServiceImpl();

            String oggetto = "Benvenuto su Delivery Home Restaurant!";
            String corpo = """
                    Ciao %s %s,

                    Grazie per esserti registrato su Delivery Home Restaurant!
                    Siamo felici di averti con noi 🍽️

                    Puoi accedere alla tua area personale e iniziare a ordinare i tuoi piatti preferiti.

                    A presto,
                    Il team di Delivery
            """.formatted(nome, cognome);

            try {
                emailServiceImpl.sendEmail(email, oggetto, corpo);
            } catch (MessagingException e) {
                request.setAttribute("warning", "Registrazione riuscita, ma l'invio dell'email è fallito.");
            }
            
            
            
            
            
            
            response.sendRedirect(request.getContextPath() + "/User/home/");    
        } else {
            // Registrazione fallita, mostra pagina di registrazione con errore
            UtilFlashMessages.addMessage(request, "error" , "Utente già registrato, esegui l'accesso!");
            response.sendRedirect(request.getContextPath() + "/User/showRegisterForm/");
        }
    }
        
    public void logoutUser(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException{
        
            
        
            try {
           // 1. Ottieni la sessione corrente (senza crearne una nuova se non esiste)
           HttpSession session = UtilSession.getSession(request);

           // 2. Se la sessione esiste, invalidalà
           if (session != null) {
               session.invalidate(); // Distrugge completamente la sessione
           }

           // 3. Reindirizza alla home page
           response.sendRedirect(request.getContextPath() + "/User/home");

       } catch (Exception e) {
           
           response.sendRedirect(request.getContextPath() + "/User/showProfile");
           

       }

    }
    
 public void modifyProfile(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

    EntityManager em = (EntityManager) request.getAttribute("em");
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);

    HttpSession session = UtilSession.getSession(request);
    String newName = InputSanitizer.sanitize(request.getParameter("newName"));
    String newSurname = InputSanitizer.sanitize(request.getParameter("newSurname"));

    if (session != null) {
        EUtente utenteSessione = (EUtente) session.getAttribute("utente");

        // Carico l'utente aggiornato dal DB
        EUtente utente = utenteDAO.findById(utenteSessione.getId());

        // Modifico l'utente già gestito da questo EntityManager
        utente.setNome(newName);
        utente.setCognome(newSurname);

        utenteDAO.save(utente); // Merge non genera INSERT

        // aggiorna anche in sessione se serve
        session.setAttribute("utente", utente);

        UtilFlashMessages.addMessage(request, "success", "Modifiche effettuate con successo!");
        response.sendRedirect(request.getContextPath() + "/User/showProfile");
    } 
}
 
 public void changePassword(HttpServletRequest request, HttpServletResponse response, String[] params)
         throws ServletException, IOException, TemplateException{
     
     EntityManager em = (EntityManager) request.getAttribute("em");
     EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
     HttpSession sessione = UtilSession.getSession(request);
     
     try{
         String oldPassword = InputSanitizer.sanitize(request.getParameter("oldPassword"));
         String newPassword = InputSanitizer.sanitize(request.getParameter("newPassword"));
         if(sessione != null && sessione.getAttribute("utente") != null){
         EUtente sessionUser = (EUtente) sessione.getAttribute("utente");
         EUtente currentUser = utenteDAO.findById(sessionUser.getId());
            if(BCrypt.checkpw(oldPassword, currentUser.getPassword())){
                currentUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt() ));
                utenteDAO.save(currentUser);
                UtilFlashMessages.addMessage(request, "success", "Password modificata con successo!");
                response.sendRedirect(request.getContextPath() + "/User/showProfile");
            }
            else {
                UtilFlashMessages.addMessage(request, "error", "Errore nella modifica della password, riprovare!");
                response.sendRedirect(request.getContextPath() + "/User/showProfile");
            }
         
        }
     } catch(Exception e){
         
                UtilFlashMessages.addMessage(request, "error", "Errore nella modifica della password, riprovare!");
                response.sendRedirect(request.getContextPath() + "/User/showProfile");
         
     }
 }
     
    /* Differenza tra Attached e Detached entity */  
    public void addAddress(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {

        EntityManager em = (EntityManager) request.getAttribute("em");

        try {
            String via = request.getParameter("via");
            String cap = request.getParameter("cap");
            String civico = request.getParameter("civico");
            String citta = request.getParameter("citta");

            // Recupera l'utente dalla sessione solo per prenderne l'ID
            ECliente clienteSession = (ECliente) UtilSession.getSession(request).getAttribute("utente");

            // Ricarica il cliente dal DB con l'EntityManager
            ECliente cliente = em.find(ECliente.class, clienteSession.getId());

            // Controllo se l'indirizzo esiste altrimenti ne creo uno nuovo
            EIndirizzo indirizzo = checkIfAddressAlreadyExistsForUser(cliente, citta, via, civico);
            indirizzo.setVia(via);
            indirizzo.setCitta(citta);
            indirizzo.setCivico(civico);
            indirizzo.setCap(cap);
            indirizzo.setAttivo(true);

            em.getTransaction().begin();

            // Salva l'indirizzo
            em.persist(indirizzo);

            em.getTransaction().commit();
            UtilFlashMessages.addMessage(request, "success", "Indirizzo aggiunto correttamente!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");

        } catch (Exception e) {
            em.getTransaction().rollback();
            UtilFlashMessages.addMessage(request, "error", "Errore nel salvataggio dell'indirizzo, riprovare!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        }
    }
    
    public EIndirizzo checkIfAddressAlreadyExistsForUser(
            ECliente cliente,
            String newCitta,
            String newVia,
            String newCivico
    ) {
        List<EIndirizzo> indirizzi = cliente.getIndirizziConsegna();

        // Normalizzazione input
        String cittaNorm = newCitta.toLowerCase();
        String viaNorm = newVia.toLowerCase();
        String civicoNorm = newCivico.toLowerCase();

        // Controllo se esiste già
        for (EIndirizzo indirizzo : indirizzi) {
            String cittaEsistente = indirizzo.getCitta().toLowerCase();
            String viaEsistente = indirizzo.getVia().toLowerCase();
            String civicoEsistente = indirizzo.getCivico().toLowerCase();

            if (cittaNorm.equals(cittaEsistente)
                    && viaNorm.equals(viaEsistente)
                    && civicoNorm.equals(civicoEsistente)) {
                return indirizzo;
            }
        }

        // Se non esiste, ne creo uno nuovo e lo associo
        EIndirizzo nuovoIndirizzo = new EIndirizzo();
        nuovoIndirizzo.addCliente(cliente);

        return nuovoIndirizzo;
    }

    public void addCreditCard(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws IOException, ServletException, TemplateException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        ECliente cliente = (ECliente) UtilSession.getSession(request).getAttribute("utente");
        ECartaCreditoDAO creditoDAO = new ECartaCreditoDAOImpl(em);

    try {
        // Validazione e parsing dei parametri
        String numeroCarta = InputSanitizer.sanitize(request.getParameter("numero_carta"));
        String nomeCarta = InputSanitizer.sanitize(request.getParameter("nome_carta"));
        String dataScadenzaStr = request.getParameter("data_scadenza"); // formato atteso: "MM/yy"
        String cvv = InputSanitizer.sanitize(request.getParameter("cvv"));
        String nomeIntestatario = InputSanitizer.sanitize(request.getParameter("nome_intestatario"));

            // Validazione base
            if (numeroCarta == null || numeroCarta.length() != 16 || !numeroCarta.matches("\\d{16}")) {
                throw new IllegalArgumentException("Numero carta non valido.");
            }
            if (cvv == null || !cvv.matches("\\d{3,4}")) {
                throw new IllegalArgumentException("CVV non valido.");
            }

            // Parsing data scadenza in formato "MM/yy"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth yearMonth = YearMonth.parse(dataScadenzaStr, formatter);
            LocalDate dataScadenza = yearMonth.atEndOfMonth(); // ultimo giorno del mese
            LocalDateTime dataScadenzaCompleta = dataScadenza.atTime(23, 59, 59);
            em.getTransaction().begin();
            //Se la carta esiste ma è disattivata la riattiviamo
            ECartaCredito cartaEsistente = creditoDAO.getCreditCardByCardNumber(numeroCarta);
            if(cartaEsistente != null) {
                cartaEsistente.setAttivo(true);
                em.persist(cartaEsistente);
            } else {
                ECartaCredito cartaCredito = new ECartaCredito();
                cartaCredito.setNumeroCarta(numeroCarta);
                cartaCredito.setNomeCarta(nomeCarta);
                cartaCredito.setDataScadenza(dataScadenzaCompleta);
                cartaCredito.setCvv(cvv);
                cartaCredito.setNomeIntestatario(nomeIntestatario);
                cartaCredito.setCliente(cliente);
                em.persist(cartaCredito);
            }
            em.getTransaction().commit();

            // Reindirizzamento al profilo utente
            UtilFlashMessages.addMessage(request, "success", "Metodo di pagamento salvato con successo!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");

        } catch (IllegalArgumentException e) {
            em.getTransaction().rollback();
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        } catch (Exception e) {
            em.getTransaction().rollback();
            UtilFlashMessages.addMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        }
    }

public void showReviewForm(HttpServletRequest request, HttpServletResponse response, String[] params) throws IOException, TemplateException{
    
    EntityManager em = (EntityManager) request.getAttribute("em");
    HttpSession session = UtilSession.getSession(request);
    String role = "";
    boolean logged = true;
    Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
    
    try{
        
        EUtente utente = AccessControlUtil.getLoggedUser(request);
        ECliente cliente = AccessControlUtil.checkUserRole(utente, ECliente.class);
        role = cliente.getRuolo();
        
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("role", role);
        data.put("logged", logged);
        
        
        response.setContentType("text/html;charset=UTF-8");
        TemplateRenderer.render(request, response, "review_form.ftl", data);
        
        
    } catch(SecurityException e){
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
            
    } catch (Exception e) {
            logged = false;
            if(session != null && session.getAttribute("utente") != null){
                EUtente utente =  (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
    } 
    
}

    public void removeAddress(HttpServletRequest request, HttpServletResponse response, String[] params) throws IOException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        EIndirizzoDAO indirizzoDAO = new EIndirizzoDAOImpl(em);
        EntityTransaction transaction = em.getTransaction();



        try{
            int indirizzoId = Integer.parseInt(request.getParameter("indirizzo_id"));
            EIndirizzo indirizzo = indirizzoDAO.getAddressById(indirizzoId);
            if(indirizzo != null){
                indirizzo.setAttivo(false);
                transaction.begin();
                em.merge(indirizzo);
                transaction.commit();
                UtilFlashMessages.addMessage(request, "success", "Indirizzo rimosso correttamente!");
                response.sendRedirect(request.getContextPath() + "/User/showProfile");
            }
        }catch(Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            UtilFlashMessages.addMessage(request, "error", "Errore nella rimozione, riprovare!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        }

    }

    public  void removeCreditCard(HttpServletRequest request, HttpServletResponse response, String[] params) throws Exception{

        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.getSession(request);
        EntityTransaction transaction = em.getTransaction();
        ECartaCreditoDAO creditoDAO = new ECartaCreditoDAOImpl(em);

        try{
            String numCarta = request.getParameter("numero_carta");
            ECartaCredito cartaCredito = creditoDAO.getCreditCardByCardNumber(numCarta);
            if(cartaCredito == null){
                throw new IllegalArgumentException("Carta di credito non trovata");
            }
            cartaCredito.setAttivo(false);
            transaction.begin();
            em.merge(cartaCredito);
            transaction.commit();
            UtilFlashMessages.addMessage(request, "success", "Metodo di pagamento rimosso con successo!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");

        }catch(Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            UtilFlashMessages.addMessage(request, "error", "Errore nella rimozione, riprovare!");
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
        }
    } 
     
}
