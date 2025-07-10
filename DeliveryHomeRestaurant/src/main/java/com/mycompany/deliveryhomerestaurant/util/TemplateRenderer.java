/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateRenderer {
    
    public static void render(HttpServletRequest request,
                          HttpServletResponse response,
                          String templateName,
                          Map<String, Object> data) throws IOException, TemplateException {

    if (data == null) {
        data = new HashMap<>();
    }

    // Aggiungo contextPath sempre
    data.putIfAbsent("contextPath", request.getContextPath());

    // Recupero sessione (senza crearla se non esiste)
    var session = request.getSession(false);

    // Default values
    String role = "";
    boolean logged = false;

    if (session != null) {
        Object userObj = session.getAttribute("utente");
        if (userObj != null) {
            logged = true;

            // Provo a ricavare il ruolo da utente (se c'è il metodo getRuolo)
            try {
                var metodoGetRuolo = userObj.getClass().getMethod("getRuolo");
                if (metodoGetRuolo != null) {
                    Object ruoloObj = metodoGetRuolo.invoke(userObj);
                    if (ruoloObj != null) {
                        role = ruoloObj.toString();
                    }
                }
            } catch (Exception e) {
                // Ignoro, significa che non è presente il metodo getRuolo
            }
        }
    }

    data.putIfAbsent("role", role);
    data.putIfAbsent("logged", logged);

    Configuration cfg = com.mycompany.deliveryhomerestaurant.FreeMarkerConfig.getConfig(request.getServletContext());
    Template template = cfg.getTemplate(templateName);

    response.setContentType("text/html;charset=UTF-8");
    template.process(data, response.getWriter());
}


    public static void mostraErrore(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String templateName,
                                    String errorMessage,
                                    String role,
                                    boolean logged) throws IOException, TemplateException {

        Configuration cfg = com.mycompany.deliveryhomerestaurant.FreeMarkerConfig.getConfig(request.getServletContext());
        Template template = cfg.getTemplate(templateName);

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("errorMessage", errorMessage);
        data.put("role", role);
        data.put("logged", logged);

        response.setContentType("text/html;charset=UTF-8");
        template.process(data, response.getWriter());
    }

    // (opzionale) puoi anche aggiungere altri metodi simili per rendering generico
}
