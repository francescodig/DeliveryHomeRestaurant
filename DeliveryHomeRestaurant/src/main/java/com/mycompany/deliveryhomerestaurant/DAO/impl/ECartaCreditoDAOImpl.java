/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

/**
 *
 * @author franc
 */
import java.util.List;

public class ECartaCreditoDAOImpl implements ECartaCreditoDAO {

    private final EntityManager em;

    public ECartaCreditoDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ECartaCredito> getAllCreditCards() {
        return em.createQuery("SELECT c FROM ECartaCredito c", ECartaCredito.class).getResultList();
    }



    @Override
    public ECartaCredito getCreditCardByUserId(int userId) {
        try {
            return em.createQuery(
                "SELECT c FROM ECartaCredito c WHERE c.cliente.id = :userId", ECartaCredito.class)
                .setParameter("userId", userId)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    public ECartaCredito getCreditCardByCardNumber(String cardNumber){
        try {
            return em.createQuery(
                "SELECT c FROM ECartaCredito c WHERE c.numeroCarta = :cardNumber", ECartaCredito.class)
                .setParameter("cardNumber", cardNumber)
                .getSingleResult();
            
            
        } catch(NoResultException e){
            return null;
        }
    }
}
