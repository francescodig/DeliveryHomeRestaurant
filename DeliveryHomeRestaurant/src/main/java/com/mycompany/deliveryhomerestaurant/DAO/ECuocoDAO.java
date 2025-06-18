/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;



import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import java.util.List;

/**
 *
 * @author franc
 */


public interface ECuocoDAO {
    List<ECuoco> getAllChefs() throws Exception;
}

