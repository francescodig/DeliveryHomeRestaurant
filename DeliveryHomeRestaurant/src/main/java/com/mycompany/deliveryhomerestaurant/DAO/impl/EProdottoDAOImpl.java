/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author franc
 */

public class EProdottoDAOImpl implements EProdottoDAO {

    private final EntityManager em;

    public EProdottoDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<EProdotto> getAllProducts() {
        return em.createQuery("SELECT p FROM EProdotto p", EProdotto.class).getResultList();
    }
    
    @Override
    public List<EProdotto> getAllInactiveProducts() {
        return em.createQuery("FROM EProdotto p WHERE p.attivo = :attivo", EProdotto.class)
                .setParameter("attivo", false)
                .getResultList();
    }
    
    @Override
    public List<EProdotto> getAllActiveProducts() {
        return em.createQuery("FROM EProdotto p WHERE p.attivo = :attivo", EProdotto.class)
                .setParameter("attivo", true)
                .getResultList();
    }
    
    @Override
    public EProdotto getProductById(int id){
        
        try {
            return em.find(EProdotto.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        
        
    }
}
