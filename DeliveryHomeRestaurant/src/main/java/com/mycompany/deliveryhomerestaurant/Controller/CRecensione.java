/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;

import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.util.UtilFlashMessages;
import com.mycompany.deliveryhomerestaurant.util.UtilSession;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author franc
 */
public class CRecensione {
    
    public void writeReview(HttpServletRequest request, HttpServletResponse response, String[] params) {
    
    HttpSession session = UtilSession.getSession(request);
    EntityManager em = (EntityManager) request.getAttribute("em");
    EUtente utente = null;

    try {
        if (session != null && session.getAttribute("utente") != null) {
            utente = (EUtente) session.getAttribute("utente");
        }

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String description = request.getParameter("description");
        int vote = 1; // valore di default

        try {
            String voteParam = request.getParameter("vote");
            if (voteParam != null) {
                int parsedVote = Integer.parseInt(voteParam);
                if (parsedVote >= 0 && parsedVote <= 5) {
                    vote = parsedVote;
                }
            }
        } catch (NumberFormatException ex) {
            // Log dell'errore o gestione alternativa
            System.err.println("Valore 'vote' non valido: " + ex.getMessage());
        }

        ERecensione recensione = new ERecensione();
        recensione.setCliente(utente);
        recensione.setDescrizione(description);
        recensione.setVoto(vote);
        recensione.setData(LocalDateTime.now());
        
        em.getTransaction().begin();
        em.persist(recensione);
        em.getTransaction().commit();
        UtilFlashMessages.addMessage(request, "success", "Recensione aggiunta con successo");
        response.sendRedirect(request.getContextPath() + "/User/showProfile");

    } catch (Exception e) {
        // Log dell'errore generico
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
        e.printStackTrace();
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'invio della recensione.");
        } catch (IOException ignored) {}
    }
}

    
}
