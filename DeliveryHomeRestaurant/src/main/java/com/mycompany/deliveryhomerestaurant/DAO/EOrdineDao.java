/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author franc
 */
public interface EOrdineDao {
    
    EOrdine getOrdineById(String id);
    List<EOrdine> getAllOrders();
    List<EOrdine> getOrdersByClient(ECliente cliente);
    List<EOrdine> getOrdersByState(String state);
    List<EOrdine> getOrdersByDataEsecuzione(LocalDateTime data);
    List<EOrdine> getOrdersByDataGiornaliera(LocalDateTime data);
    
    
    
    
    
}
