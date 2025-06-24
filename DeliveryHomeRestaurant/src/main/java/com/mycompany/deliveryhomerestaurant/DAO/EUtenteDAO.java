/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.EUtente;

/**
 *
 * @author franc
 */
public interface EUtenteDAO {
    

    //find the user by the username
    EUtente findByUsername(String username);
    
    EUtente findById(int id);
    
    // save the user
    void save(EUtente user);

    
}
