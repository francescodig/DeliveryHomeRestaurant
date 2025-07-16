/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;
import com.mycompany.deliveryhomerestaurant.DAO.ECuocoDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List

/**
 *
 * @author franc
 */


;

public class ECuocoDAOImpl implements ECuocoDAO {

    private final EntityManager em;

    public ECuocoDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ECuoco> getAllChefs() throws Exception {
        try {
            TypedQuery<ECuoco> query = em.createQuery("SELECT c FROM ECuoco c", ECuoco.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero dei cuochi", e);
        }
    }
}
