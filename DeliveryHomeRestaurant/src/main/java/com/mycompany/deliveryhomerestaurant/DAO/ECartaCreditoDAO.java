/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;



import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import java.util.List;
/**
 *
 * @author franc
 */

public interface ECartaCreditoDAO {
    List<ECartaCredito> getAllCreditCards();
    ECartaCredito getCreditCardByUserId(int userId);
}

