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


public class FrontController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String uri = request.getRequestURI();           // /DeliveryHomeRestaurant/User/home
    String contextPath = request.getContextPath();  // /DeliveryHomeRestaurant
    String path = uri.substring(contextPath.length()); // /User/home
    
    // Escludi risorse statiche
    if (path.startsWith("/resources/") || path.startsWith("/WEB-INF/")) {
        request.getRequestDispatcher(path).forward(request, response);
        return;
    }

    // Debug: stampa i path per verifica
    System.out.println("URI: " + uri);
    System.out.println("Context Path: " + contextPath);
    System.out.println("Path: " + path);

    String[] parts = path.split("/");

    // Estrai le parti correttamente (parts[0] sarà vuoto perché inizia con /)
    String controllerName = parts.length > 1 ? parts[1] : "User"; // "User" da /User/home
    String methodName = parts.length > 2 ? parts[2] : "home";    // "home" da /User/home
    String[] params = parts.length > 3 ? Arrays.copyOfRange(parts, 3, parts.length) : new String[0];

    String className = "com.mycompany.deliveryhomerestaurant.Controller.C" + controllerName;
    
    System.out.println("Trying to load: " + className + " method: " + methodName);
    
    try {
        Class<?> clazz = Class.forName(className);
        Object controller = clazz.getDeclaredConstructor().newInstance();
        java.lang.reflect.Method method = clazz.getMethod(methodName, 
            HttpServletRequest.class, HttpServletResponse.class, String[].class);
        
        method.invoke(controller, request, response, params);
    } catch (ClassNotFoundException e) {
        response.sendError(404, "Controller " + className + " non trovato");
    } catch (NoSuchMethodException e) {
        response.sendError(404, "Metodo " + methodName + " non trovato in " + className);
    } catch (Exception e) {
        throw new ServletException("Errore: " + e.getMessage(), e);
    }
}
}
