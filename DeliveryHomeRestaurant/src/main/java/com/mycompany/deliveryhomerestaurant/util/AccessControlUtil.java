/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.util;

import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AccessControlUtil {

    public static EUtente getLoggedUser(HttpServletRequest request) {
        HttpSession session = UtilSession.getSession(request);
        if (session == null) {
            throw new IllegalArgumentException("Accesso negato: sessione non trovata.");
        }

        EUtente utente = (EUtente) session.getAttribute("utente");
        if (utente == null) {
            throw new IllegalArgumentException("Accesso negato: utente non autenticato.");
        }

        return utente;
    }

    public static <T> T checkUserRole(EUtente utente, Class<T> roleClass) {
        if (!roleClass.isInstance(utente)) {
            throw new SecurityException("Accesso negato: ruolo non autorizzato.");
        }
        return roleClass.cast(utente);
    }
}
