/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import java.util.List;

/**
 *
 * @author franc
 */



public interface EClienteDAO {

    boolean verify(String field, Object value);

    List<ECliente> getAllClients();

    ECliente getClientById(int userId);
}
