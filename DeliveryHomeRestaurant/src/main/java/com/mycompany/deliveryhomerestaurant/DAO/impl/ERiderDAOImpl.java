/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.ERiderDAO;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author franc
 */
public class ERiderDAOImpl implements ERiderDAO {
    
    private final EntityManager em;

    public ERiderDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ERider> getAllRiders() throws Exception {
        try {
            TypedQuery<ERider> query = em.createQuery("SELECT r FROM ERider r", ERider.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero dei cuochi", e);
        }
    }
    
}
