/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author franc
 */


public class FrontController extends HttpServlet {
    
    private static final EntityManagerFactory emf = jakarta.persistence.Persistence.createEntityManagerFactory("myPersistenceUnit");

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        request.setAttribute("em", em);

        try {
            String uri = request.getRequestURI();           
            String contextPath = request.getContextPath();  
            String path = uri.substring(contextPath.length()); 

            if (path.startsWith("/resources/") || path.startsWith("/WEB-INF/")) {
                request.getRequestDispatcher(path).forward(request, response);
                return;
            }

            String[] parts = path.split("/");
            String controllerName = parts.length > 1 ? parts[1] : "User"; 
            String methodName = parts.length > 2 ? parts[2] : "home";    
            String[] params = parts.length > 3 ? Arrays.copyOfRange(parts, 3, parts.length) : new String[0];

            String className = "com.mycompany.deliveryhomerestaurant.Controller.C" + controllerName;

            Class<?> clazz = Class.forName(className);
            Object controller = clazz.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method method = clazz.getMethod(methodName, 
                HttpServletRequest.class, HttpServletResponse.class, String[].class);

            method.invoke(controller, request, response, params);

        } catch (ClassNotFoundException e) {
            response.sendError(404, "Controller non trovato");
        } catch (NoSuchMethodException e) {
            response.sendError(404, "Metodo non trovato");
        } catch (Exception e) {
            throw new ServletException("Errore: " + e.getMessage(), e);
        } finally {
            if (em.isOpen()) {
                em.close(); // 🔒 IMPORTANTE!
            }
        }
    }
}

