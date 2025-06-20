/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author franc
 */

@WebServlet("/Delivery/*")  // <-- questa riga è ESSENZIALE
public class FrontController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();           // /Delivery/User/home/5
        String contextPath = request.getContextPath();  // /Delivery
        String path = uri.substring(contextPath.length()); // /User/home/5

        // Rimuovi la parte iniziale "/Delivery/"
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        // Ignora risorse statiche
        if (path.matches(".*\\.(css|js|png|jpg|jpeg|woff2|ico|svg)$")) {
            return;
        }

        String[] parts = path.split("/");

        String controllerName = parts.length > 0 && !parts[0].isEmpty() ? parts[0] : "User";
        String methodName = parts.length > 1 ? parts[1] : "home";
        String[] params = parts.length > 2 ? Arrays.copyOfRange(parts, 2, parts.length) : new String[0];

        String className = "com.mycompany.deliveryhomerestaurant.Controller.C" + controllerName;

        try {
            System.out.println("Class name: " + className);
            Class<?> clazz = Class.forName(className);
            Object controller = clazz.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class, String[].class);
            method.invoke(controller, request, response, params);
        } catch (ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Controller non trovato");
        } catch (NoSuchMethodException e) {
            response.sendRedirect(contextPath + "/User/home");
        } catch (Exception e) {
            throw new ServletException("Errore durante la chiamata del metodo", e);
        }
    }
}

