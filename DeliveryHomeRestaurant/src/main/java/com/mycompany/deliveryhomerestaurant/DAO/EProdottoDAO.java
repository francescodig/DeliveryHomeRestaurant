/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;


import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import java.util.List;


/**
 *
 * @author franc
 */
public interface EProdottoDAO {
    List<EProdotto> getAllProducts();
    List<EProdotto> getAllInactiveProducts();
    List<EProdotto> getAllActiveProducts();
    EProdotto getProductById(int id);
}
