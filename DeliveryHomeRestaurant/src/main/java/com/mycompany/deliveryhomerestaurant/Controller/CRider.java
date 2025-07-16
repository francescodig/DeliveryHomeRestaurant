package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EClienteDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EOrdineDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EUtenteDAOImpl;
import com.mycompany.deliveryhomerestaurant.FreeMarkerConfig;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.MailServiceImpl;
import com.mycompany.deliveryhomerestaurant.util.AccessControlUtil;
import com.mycompany.deliveryhomerestaurant.util.TemplateRenderer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
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

            List<EOrdine> ordiniPronti = ordineDAO.getOrdersByState("pronto");
            List<EOrdine> ordiniInConsegna = ordineDAO.getOrdersByStateNotMine("in_consegna", rider);
            List<EOrdine> ordiniRider = ordineDAO.getOrdersByRider(rider);
            
            
            Map<String, Object> data = new HashMap<>();
            data.put("orders", ordiniPronti);
            data.put("ordersOnDelivery", ordiniInConsegna);
            data.put("myOrders", ordiniRider);
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
    EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
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

        // Controlla se l'ordine ha già un rider assegnato diverso dall'attuale
        ERider riderConsegna = ordine.getRiderConsegna();
        if (riderConsegna != null && !(riderConsegna.getId().equals(rider.getId()))) {
            em.getTransaction().rollback();
            TemplateRenderer.mostraErrore(
                request,
                response,
                "rider_error.ftl",
                "L'ordine è già stato preso in carico da un altro rider.",
                rider.getRuolo(),
                true
            );
            return;
        }

        ordine.setStato(nuovoStato);
        // Se non ha rider, lo assegna
        ordine.setRiderConsegna(rider);
        
        if("in_consegna".equalsIgnoreCase(nuovoStato)){
            
            MailServiceImpl emailServiceImpl = new MailServiceImpl();

            String oggetto = "Ordine in consegna - Delivery Home Restaurant";

            String corpo = """
                
                
                Ciao %s,
                Il tuo ordine %s è stato preso in consegna dal rider %s.
                Grazie per averci scelto!
                Il team di Delivery
                """.formatted(ordine.getCliente().getNome(), ordine.getId(), ordine.getRiderConsegna().getCodiceRider());

            try {
                emailServiceImpl.sendEmail(ordine.getCliente().getEmail(), oggetto, corpo);
            } catch (MessagingException e) {
                request.setAttribute("warning", "L'invio dell'email è fallito.");
            }
            
        }

        

        // Se è stato consegnato, salva anche data e ora
        if ("consegnato".equalsIgnoreCase(nuovoStato)) {
            ordine.setDataConsegna(LocalDateTime.now());
            
            
                                    
            MailServiceImpl emailServiceImpl = new MailServiceImpl();

            String oggetto = "Ordine rifiutato - Delivery Home Restaurant";

            String corpo = """
                Il tuo ordine è stato consegnato
                
                Ciao %s,
                L'ordine %s è stato consegnato.
                Grazie per averci scelto!
                Il team di Delivery
                """.formatted(ordine.getCliente().getNome(), ordine.getId());

            try {
                emailServiceImpl.sendEmail(ordine.getCliente().getEmail(), oggetto, corpo);
            } catch (MessagingException e) {
                request.setAttribute("warning", "L'invio dell'email è fallito.");
            }

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
