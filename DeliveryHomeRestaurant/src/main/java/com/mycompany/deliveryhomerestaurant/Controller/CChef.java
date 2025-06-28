/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
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
/**
 *
 * @author simone
 */
public class CChef {
    
    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, TemplateException, ServletException {

    Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());

    EntityManager em = (EntityManager) request.getAttribute("em");
    HttpSession session = UtilSession.getSession(request);
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
    EOrdineDao ordineDAO = new EOrdineDAOImpl(em);

    try {
        EUtente utente = (EUtente) session.getAttribute("utente");
        String role = "";
        
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessione non trovata.");
            return;
        }

        if (utente == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non autenticato.");
            return;
        }

        ECuoco chefAttached = (ECuoco) utenteDAO.findById(utente.getId());

        if (chefAttached != null) {
            List<EOrdine> ordiniChef = ordineDAO.getOrdersByState("in_preparazione");
            role = chefAttached.getRuolo();

            Template template = cfg.getTemplate("chef_orders.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", ordiniChef);
            data.put("role", role);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato: non sei un cuoco.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero degli ordini.");
    }
}
    
    public void cambiaStatoOrdine(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, TemplateException, ServletException{
        
        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        
        String ordineId = request.getParameter("ordineId");
        String nuovoStato = request.getParameter("stato");
        
        EOrdine ordine = ordineDAO.getOrdineById(ordineId);
        
        try{
                       
            em.getTransaction().begin();
            ordine.setStato(nuovoStato);
            if(nuovoStato.equals("pronto")){
                ordine.setDataConsegna(LocalDateTime.now());
            }
            em.flush();
            em.getTransaction().commit();
            
            response.sendRedirect(request.getContextPath() + "/Chef/showOrders");  
            
        } catch(Exception e){
            
          em.getTransaction().rollback(); // meglio fare rollback in caso di errore
          throw new ServletException("Errore nel cambio stato ordine", e);
            
        }
        
        
    }

}
    

