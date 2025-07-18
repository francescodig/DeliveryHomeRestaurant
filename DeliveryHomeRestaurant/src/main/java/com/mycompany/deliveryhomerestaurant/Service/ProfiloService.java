/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Service;

import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author franc
 */
//Interfaccia per metodi di registrazione e login 
public interface ProfiloService {
    
    
    boolean Register(EUtente user);
    EUtente login(String username, String password, HttpSession session);
}

