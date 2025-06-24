package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EMenuDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class CUser {


    public void home(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        
        try {
           


            Template template = cfg.getTemplate("home.ftl");

            ERecensioneDAO recensioneDAO = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDAO.getAllReviews();
            Collections.shuffle(allReviews);
            List<ERecensione> reviews = allReviews.subList(0, Math.min(3, allReviews.size()));

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("reviews", reviews);

            HttpSession session = request.getSession(false);
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
            }
            data.put("logged", logged);

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
        
        try {
            Template template = cfg.getTemplate("menu.ftl");

            EMenuDAO menuDAO = new EMenuDAOImpl(em);
            List<Map<String, Object>> menu = menuDAO.getMenu();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("menu", menu);

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
         
        try {

            Template template = cfg.getTemplate("order.ftl");

            EMenuDAO menuDAO = new EMenuDAOImpl(em);
            List<Map<String, Object>> menu = menuDAO.getMenu();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("menu", menu);

            HttpSession session = request.getSession(false);
            boolean logged = false;
            if (session != null && session.getAttribute("utente") != null) {
                logged = true;
            }
            data.put("logged", logged);

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
        
        
        try {
            HttpSession session = request.getSession(false);
            ECliente utente = null;
            boolean logged = false;
            if (session != null) {
                utente = (ECliente) session.getAttribute("utente");
                logged = true;
            }
            if (utente == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

     
            Template template = cfg.getTemplate("miei_ordini.ftl");

            EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
            List<EOrdine> mieiOrdini = ordineDAO.getOrdersByClient(utente);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", mieiOrdini);
            data.put("logged", logged);

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
        
        try {

            HttpSession session = request.getSession(false);
            ECliente utente = null;
            EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
            boolean logged = false;

            if (session != null) {
                utente =  (ECliente) session.getAttribute("utente");
                logged = true;

                Template template = cfg.getTemplate("account.ftl");
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());
                data.put("utente", utente);
                data.put("logged", logged);

                response.setContentType("text/html;charset=UTF-8");
                template.process(data, response.getWriter());
            } else {
                logged = false;
                Template template = cfg.getTemplate("login.ftl");
                response.setContentType("text/html;charset=UTF-8");
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.getContextPath());

                template.process(data, response.getWriter());
            }

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        } 
    }
    
    public void loginUser(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws ServletException, IOException, TemplateException {
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        try {
            HttpSession session = request.getSession(true);


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

        
        try{
            Template template = cfg.getTemplate("register.ftl");
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
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
            HttpSession session = request.getSession(true);
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
           HttpSession session = request.getSession(false);

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

    HttpSession session = request.getSession(false);
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
    } else {
        response.sendRedirect(request.getContextPath() + "/User/login");
    }
}
 
 public void changePassword(HttpServletRequest request, HttpServletResponse response, String[] params)
         throws ServletException, IOException, TemplateException{
     
     EntityManager em = (EntityManager) request.getAttribute("em");
     EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
     HttpSession sessione = request.getSession(false);
     
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
    
}
