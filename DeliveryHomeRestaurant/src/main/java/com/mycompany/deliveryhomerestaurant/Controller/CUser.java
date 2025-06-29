package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECartaCreditoDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
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
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class CUser{


    public void home(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        String role = "";
        
        try {
           


            Template template = cfg.getTemplate("home.ftl");

            ERecensioneDAO recensioneDAO = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDAO.getAllReviews();
            Collections.shuffle(allReviews);
            List<ERecensione> reviews = allReviews.subList(0, Math.min(3, allReviews.size()));

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("reviews", reviews);
            
            HttpSession session = UtilSession.getSession(request);
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
                EUtente utente = (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            data.put("logged", logged);
            data.put("role", role);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
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
            Template template = cfg.getTemplate("menu.ftl");

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
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        }
    }

    public void order(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
         EntityManager em = (EntityManager) request.getAttribute("em");
         Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
         String role = "";
         
        try {

            Template template = cfg.getTemplate("order.ftl");

            EMenuDAO menuDAO = new EMenuDAOImpl(em);
            List<Map<String, Object>> menu = menuDAO.getMenu();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("menu", menu);

            HttpSession session = UtilSession.getSession(request);
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
                EUtente utente = (EUtente) session.getAttribute("utente");
                role = utente.getRuolo();
            }
            data.put("logged", logged);
            data.put("role", role);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        }
    }

    public void showMyOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        
        
        try {
            ECliente utente = null;
            boolean logged = false;
            if (session != null && session.getAttribute("utente")!=null) {
                utente = (ECliente) session.getAttribute("utente");
                logged = true;
                role = utente.getRuolo();
            }
            if (utente == null) {
                response.sendRedirect(request.getContextPath() + "/showProfile");
                return;
            }

     
            Template template = cfg.getTemplate("miei_ordini.ftl");

            EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
            List<EOrdine> mieiOrdini = ordineDAO.getOrdersByClient(utente);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", mieiOrdini);
            data.put("logged", logged);
            data.put("role",role);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        } 
    }

    public void showProfile(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        HttpSession session = UtilSession.getSession(request);
        String role = "";
        
        try {

            
            EUtente utente = null;
            EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
            boolean logged = false;

            if (session != null && session.getAttribute("utente")!=null) {
                utente =  (EUtente) session.getAttribute("utente");
                EUtente attachedUser = utenteDAO.findById(utente.getId());
                logged = true;
                role = utente.getRuolo();

               

                Template template = cfg.getTemplate("account.ftl");
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());
                                
                if(role == "cliente"){
                    ECliente cliente = (ECliente) attachedUser; 
                    data.put("indirizzi", cliente.getIndirizziConsegna() );
                    data.put("carte_credito", cliente.getMetodiPagamento());
                    
                }
               
                data.put("role", utente.getRuolo());
                data.put("utente", utente);
                data.put("logged", logged);
                data.put("role", role);

                response.setContentType("text/html;charset=UTF-8");
                template.process(data, response.getWriter());
            } else {
                logged = false;
                Template template = cfg.getTemplate("login.ftl");
                response.setContentType("text/html;charset=UTF-8");
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());
                data.put("role",role);

                template.process(data, response.getWriter());
            }

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        } 
    }
    
    public void loginUser(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        HttpSession session = UtilSession.startSession(request);
        try {
            


            // se l'utente è gia in sessione 
            if(session.getAttribute("utente") != null){

                response.sendRedirect(request.getContextPath() + "/User/home/");

            }
            else{
                 // 1. Validazione parametri
                String email = request.getParameter("username");
                String password = request.getParameter("password");
                EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
                ProfiloService service = new ProfiloServiceImpl(utenteDAO);
                EUtente utenteLogin = service.login(email, password, session);

                if(utenteLogin != null) {
                    response.sendRedirect(request.getContextPath() + "/User/home/");
                    return;
                } else {
                    request.setAttribute("error", "Credenziali non valide");
                    request.getRequestDispatcher("/WEB-INF/views/login.ftl").forward(request, response);
                }


            }
        }catch (Exception e) {
            System.err.println("Errore durante il login:");
            e.printStackTrace();
        } 
    }
    
    
    public void showRegisterForm(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        String role = "";

        
        try{
            Template template = cfg.getTemplate("register.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("role",role);
            template.process(data, response.getWriter());
            
            
        }catch(Exception e){
            
        }
  
        
    }
    
    public void registerUser(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException{
        
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String ruolo = request.getParameter("role");
        
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
            response.sendRedirect(request.getContextPath() + "/User/home/");    
        } else {
            // Registrazione fallita, mostra pagina di registrazione con errore
            request.setAttribute("error", "Registrazione fallita");
            request.getRequestDispatcher("/WEB-INF/views/register.ftl").forward(request, response);
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
           // 4. Gestione degli errori
           e.printStackTrace();
           response.sendRedirect(request.getContextPath() + "/error?message=logout_failed");
       }

    }
    
 public void modifyProfile(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {

    EntityManager em = (EntityManager) request.getAttribute("em");
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);

    HttpSession session = UtilSession.getSession(request);
    String newName = request.getParameter("newName");
    String newSurname = request.getParameter("newSurname");

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

        
        response.sendRedirect(request.getContextPath() + "/User/showProfile");
    } 
}
 
 public void changePassword(HttpServletRequest request, HttpServletResponse response, String[] params)
         throws ServletException, IOException, TemplateException{
     
     EntityManager em = (EntityManager) request.getAttribute("em");
     EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
     HttpSession sessione = UtilSession.getSession(request);
     
     try{
         String oldPassword = request.getParameter("oldPassword");
         String newPassword = request.getParameter("newPassword");
         if(sessione != null && sessione.getAttribute("utente") != null){
         EUtente sessionUser = (EUtente) sessione.getAttribute("utente");
         EUtente currentUser = utenteDAO.findById(sessionUser.getId());
            if(BCrypt.checkpw(oldPassword, currentUser.getPassword())){
                currentUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt() ));
                utenteDAO.save(currentUser);
                response.sendRedirect(request.getContextPath() + "/User/");
            }
        else {
            response.sendRedirect(request.getContextPath() + "/User/showProfile");
         }
         
        }
     } catch(Exception e){
         
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

        // Crea l'indirizzo
        EIndirizzo indirizzo = new EIndirizzo();
        indirizzo.setVia(via);
        indirizzo.setCitta(citta);
        indirizzo.setCivico(civico);
        indirizzo.setCap(cap);

        em.getTransaction().begin();

        // Salva l'indirizzo
        em.persist(indirizzo);

        // Collega indirizzo al cliente
        cliente.getIndirizziConsegna().add(indirizzo);

        // Il cliente è managed, quindi non serve merge
        

        em.getTransaction().commit();

        response.sendRedirect(request.getContextPath() + "/User/showProfile");

    } catch (Exception e) {
        em.getTransaction().rollback();
        response.sendRedirect(request.getContextPath() + "/User/showProfile");
        e.printStackTrace();
    }
}

public void addCreditCard(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, ServletException, TemplateException {

    EntityManager em = (EntityManager) request.getAttribute("em");
    ECliente cliente = (ECliente) UtilSession.getSession(request).getAttribute("utente");

    try {
        // Validazione e parsing dei parametri
        String numeroCarta = request.getParameter("numero_carta");
        String nomeCarta = request.getParameter("nome_carta");
        String dataScadenzaStr = request.getParameter("data_scadenza"); // formato atteso: "MM/yy"
        String cvv = request.getParameter("cvv");
        String nomeIntestatario = request.getParameter("nome_intestatario");

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

        // Costruzione oggetto carta
        ECartaCredito cartaCredito = new ECartaCredito();
        cartaCredito.setNumeroCarta(numeroCarta);
        cartaCredito.setNomeCarta(nomeCarta);
        cartaCredito.setDataScadenza(dataScadenzaCompleta);
        cartaCredito.setCvv(cvv);
        cartaCredito.setNomeIntestatario(nomeIntestatario);
        cartaCredito.setCliente(cliente);

        // Persistenza
        em.getTransaction().begin();
        em.persist(cartaCredito);
        em.getTransaction().commit();

        // Reindirizzamento al profilo utente
        response.sendRedirect(request.getContextPath() + "/User/showProfile");

    } catch (IllegalArgumentException e) {
        em.getTransaction().rollback();
        // puoi gestire il messaggio con una setResponse o logging
        e.printStackTrace();
    } catch (PersistenceException e) {
        em.getTransaction().rollback();
        e.printStackTrace();
    } catch (Exception e) {
        em.getTransaction().rollback();
        e.printStackTrace();
    }
}

public void showReviewForm(HttpServletRequest request, HttpServletResponse response, String[] params) throws IOException{
    
    EntityManager em = (EntityManager) request.getAttribute("em");
    HttpSession session = UtilSession.getSession(request);
    String role = "";
    Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
    
    try{
        
        if(session != null && session.getAttribute("utente") != null){
            EUtente utente = (EUtente) session.getAttribute("utente");
            role = utente.getRuolo();
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("role", role);
        Template template = cfg.getTemplate("review_form.ftl");
        
        response.setContentType("text/html;charset=UTF-8");
        template.process(data , response.getWriter() );
        
        
    }catch(Exception e){
        
    }
    
    
}
     
     
     
     
     
 
    
}
