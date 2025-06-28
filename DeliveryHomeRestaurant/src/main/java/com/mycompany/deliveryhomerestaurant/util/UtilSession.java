package com.mycompany.deliveryhomerestaurant.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UtilSession {

    // Avvia la sessione se non esiste
    public static HttpSession startSession(HttpServletRequest request) {
        return request.getSession(true); // crea se non esiste
    }

    // Ottiene la sessione esistente, oppure null se non attiva
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false); // NON crea se non esiste
    }

    // Controlla se la sessione è attiva
    public static boolean isActive(HttpServletRequest request) {
        return getSession(request) != null;
    }

    // Imposta un attributo nella sessione
    public static void set(HttpServletRequest request, String key, Object value) {
        HttpSession session = startSession(request);
        session.setAttribute(key, value);
    }

    // Ottiene un attributo dalla sessione
    public static Object get(HttpServletRequest request, String key) {
        HttpSession session = getSession(request);
        return (session != null) ? session.getAttribute(key) : null;
    }

    // Controlla se esiste un attributo nella sessione
    public static boolean exists(HttpServletRequest request, String key) {
        HttpSession session = getSession(request);
        return session != null && session.getAttribute(key) != null;
    }

    // Rimuove un attributo dalla sessione
    public static void remove(HttpServletRequest request, String key) {
        HttpSession session = getSession(request);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    // Invalida l'intera sessione
    public static void destroy(HttpServletRequest request) {
        HttpSession session = getSession(request);
        if (session != null) {
            session.invalidate();
        }
    }
}
