/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import com.mycompany.deliveryhomerestaurant.DAO.ECarrelloDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECarrello;
import com.mycompany.deliveryhomerestaurant.Model.EItemCarrello;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;


/**
 *
 * @author franc
 */


public class ECarrelloDAOImpl implements ECarrelloDAO {

    private final EntityManager em;

    public ECarrelloDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public ECarrello getCartByClientId(int clientId) {
        try {
            TypedQuery<ECarrello> query = em.createQuery(
                "SELECT c FROM ECarrello c WHERE c.cliente.id = :clientId", ECarrello.class
            );
            query.setParameter("clientId", clientId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addOrUpdateItemToCart(ECarrello cart, EItemCarrello item) {
        em.getTransaction().begin();

        boolean updated = false;

        for (EItemCarrello itemCart : cart.getCarrelloItems()) {
            EProdotto p1 = itemCart.getProdotto();
            EProdotto p2 = item.getProdotto();

            if (p1.getId().equals(p2.getId())) {
                itemCart.setQuantita(itemCart.getQuantita() + item.getQuantita());
                em.merge(itemCart);
                updated = true;
                break;
            }
        }

        if (!updated) {
            em.persist(item);
        }

        em.getTransaction().commit();
    }

   @Override
    public void removeOrUpdateItemFromCart(int cartId, EItemCarrello item) {
        em.getTransaction().begin();

        ECarrello cart = em.find(ECarrello.class, cartId);
        if (cart == null) {
            em.getTransaction().rollback();
            return;
        }

        EItemCarrello itemToRemove = null;

        for (EItemCarrello itemCart : cart.getCarrelloItems()) {
            if (itemCart.getProdotto().getId().equals(item.getProdotto().getId())) {
                int q = itemCart.getQuantita();
                int q_r = item.getQuantita();

                if (q_r >= q) {  // Modificato per rimuovere se la quantità è >=
                    itemToRemove = itemCart;
                    cart.getCarrelloItems().remove(itemCart); // Rimuovi dalla collezione
                } else {
                    itemCart.setQuantita(q - q_r);
                    em.merge(itemCart);
                }
                break;
            }
        }

        if (itemToRemove != null) {
            // Assicurati che l'item sia gestito
            EItemCarrello managedItem = em.contains(itemToRemove) ? itemToRemove : em.merge(itemToRemove);
            em.remove(managedItem);
        }

        em.getTransaction().commit();
    }
}
