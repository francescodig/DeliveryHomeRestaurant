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
    

 
    EUtente findByUsername(String username);
    
    EUtente findById(int id);
    
    void save(EUtente user);

    
}
