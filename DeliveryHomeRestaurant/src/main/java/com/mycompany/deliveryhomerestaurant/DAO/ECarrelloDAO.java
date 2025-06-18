/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;
import com.mycompany.deliveryhomerestaurant.Model.ECarrello;
import com.mycompany.deliveryhomerestaurant.Model.EItemCarrello;

/**
 *
 * @author franc
 */

public interface ECarrelloDAO {

    ECarrello getCartByClientId(int clientId);

    void addOrUpdateItemToCart(ECarrello cart, EItemCarrello item);

    void removeOrUpdateItemFromCart(int cartId, EItemCarrello item);
}

