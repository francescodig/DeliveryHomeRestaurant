package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.AccessControlUtil;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRider {

    public void showOrders(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws IOException, TemplateException, ServletException {

        Configuration cfg = FreeMarkerConfig.getConfig(request.getServletContext());
        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        String role = "";
        boolean logged = true;

        try {
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ERider rider = AccessControlUtil.checkUserRole(utente, ERider.class);
            role = rider.getRuolo();

            List<EOrdine> ordiniRider = ordineDAO.getOrdersByState("pronto");
            
            Map<String, Object> data = new HashMap<>();
            data.put("orders", ordiniRider);
            data.put("role", role);

            TemplateRenderer.render(request, response, "rider_orders.ftl", data);


        } catch (SecurityException e) {
            logged = false;
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }

    public void cambiaStatoOrdine(HttpServletRequest request, HttpServletResponse response, String[] params)
            throws IOException, TemplateException, ServletException {

        EntityManager em = (EntityManager) request.getAttribute("em");
        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        String role = "";
        boolean logged = true;

        try {
            EUtente utente = AccessControlUtil.getLoggedUser(request);
            ERider rider = AccessControlUtil.checkUserRole(utente, ERider.class);
            role = rider.getRuolo();

            String ordineId = request.getParameter("ordineId");
            String nuovoStato = request.getParameter("stato");


            EOrdine ordine = ordineDAO.getOrdineById(ordineId);

            em.getTransaction().begin();
            ordine.setStato(nuovoStato);
            if ("consegnato".equals(nuovoStato)) {
                ordine.setDataConsegna(LocalDateTime.now());
            }
            em.flush();
            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/Rider/showOrders");

        } catch (SecurityException e) {
            logged = false;
            TemplateRenderer.mostraErrore(request, response, "access_denied.ftl", e.getMessage(), role, logged);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            TemplateRenderer.mostraErrore(request, response, "generic_error.ftl", e.getMessage(), role, logged);
        }
    }
}
