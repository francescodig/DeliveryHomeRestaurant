/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Service;

import com.mycompany.deliveryhomerestaurant.Model.EUtente;

/**
 *
 * @author franc
 */
public interface ProfiloService {
    
    
    boolean Register(EUtente user);
    boolean login(String username, String password);
}

