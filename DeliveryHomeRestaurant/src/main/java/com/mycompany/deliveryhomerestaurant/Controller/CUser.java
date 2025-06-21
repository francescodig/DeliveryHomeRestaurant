package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EMenuDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ERecensioneDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EElencoProdotti;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CUser {

    public void home(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        try {
            Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
            Template template = cfg.getTemplate("home.ftl");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
            EntityManager em = emf.createEntityManager();

            ERecensioneDAO recensioneDAO = new ERecensioneDAOImpl(em);
            List<ERecensione> allReviews = recensioneDAO.getAllReviews();
            
            // Mischia la lista
            Collections.shuffle(allReviews);

            // Prendi al massimo 3 elementi
            List<ERecensione> reviews = allReviews.subList(0, Math.min(3, allReviews.size()));

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("reviews", reviews);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        }
    }

    public void mostraMenu(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        try {
            Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
            Template template = cfg.getTemplate("menu.ftl");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
            EntityManager em = emf.createEntityManager();

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
        try {
            Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
            Template template = cfg.getTemplate("order.ftl");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
            EntityManager em = emf.createEntityManager();

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
    
    public void showMyOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException, TemplateException {
        try {
            Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
            Template template = cfg.getTemplate("miei_ordini.ftl");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
            EntityManager em = emf.createEntityManager();
            
            EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
            EClienteDAO clienteDAO = new EClienteDAOImpl(em);
            ECliente cliente = clienteDAO.getClientById(10);
            List<EOrdine> mieiOrdini = ordineDAO.getOrdersByClient(cliente);
            
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", mieiOrdini);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());
            
        }
        catch (Exception e) {
            throw new ServletException("Errore nel processing del template", e);
        }
    }
            
            

    

    public void register(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws ServletException, IOException {
        response.getWriter().write("Pagina registrazione utente");
    }
}
