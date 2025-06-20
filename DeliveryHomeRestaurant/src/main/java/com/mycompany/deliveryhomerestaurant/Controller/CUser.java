/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Controller;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;


/**
 *
 * @author franc
 */
public class CUser {

    public void home(HttpServletRequest request, HttpServletResponse response, String[] params) throws ServletException, IOException {
        // Logica, per esempio recupera dati e mostra pagina
        response.getWriter().write("Benvenuto nella home user!");
    }

    public void register(HttpServletRequest request, HttpServletResponse response, String[] params) throws ServletException, IOException {
        // Logica per registrazione
        response.getWriter().write("Pagina registrazione utente");
    }
}

