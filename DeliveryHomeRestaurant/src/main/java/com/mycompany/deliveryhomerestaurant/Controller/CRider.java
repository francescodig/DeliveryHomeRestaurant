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
import com.mycompany.deliveryhomerestaurant.Model.ERider;
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
 * @author franc
 */
public class CRider {
    

    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
        throws IOException, TemplateException, ServletException {

    Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());

    EntityManager em = (EntityManager) request.getAttribute("em");
    HttpSession session = UtilSession.getSession(request);
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
    EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
    String role = "";

    try {
        EUtente utente = (EUtente) session.getAttribute("utente");
        
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessione non trovata.");
            return;
        }

        if (utente == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non autenticato.");
            return;
        }

        ERider riderAttached = (ERider) utenteDAO.findById(utente.getId());

        if (riderAttached != null) {
            
            role = riderAttached.getRuolo();
            List<EOrdine> ordiniRider = ordineDAO.getOrdersByState("pronto");

            Template template = cfg.getTemplate("rider_orders.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("orders", ordiniRider);
            data.put("role", role);

            response.setContentType("text/html;charset=UTF-8");
            template.process(data, response.getWriter());
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato: non sei un rider.");
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
            if(nuovoStato.equals("consegnato")){
                ordine.setDataConsegna(LocalDateTime.now());
            }
            em.flush();
            em.getTransaction().commit();
            
            response.sendRedirect(request.getContextPath() + "/Rider/showOrders");
            
            
            
            
            
            
        } catch(Exception e){
            
          em.getTransaction().rollback(); // meglio fare rollback in caso di errore
          throw new ServletException("Errore nel cambio stato ordine", e);
            
        }
        
        
    }

}
