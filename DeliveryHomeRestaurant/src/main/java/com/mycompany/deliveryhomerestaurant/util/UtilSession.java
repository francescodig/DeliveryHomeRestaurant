package com.mycompany.deliveryhomerestaurant.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UtilSession {

    // Avvia la sessione se non esiste
    public static HttpSession startSession(HttpServletRequest request) {
        return request.getSession(true); 
    }

    // Ottiene la sessione esistente, oppure null se non attiva
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false); 
    }

    // Invalida l'intera sessione
    public static void destroy(HttpServletRequest request) {
        HttpSession session = getSession(request);
        if (session != null) {
            session.invalidate();
        }
    }
}
