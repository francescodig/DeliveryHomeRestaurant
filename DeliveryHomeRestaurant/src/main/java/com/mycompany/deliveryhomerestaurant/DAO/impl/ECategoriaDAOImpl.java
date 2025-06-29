/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

/**
 *
 * @author simone
 */

import com.mycompany.deliveryhomerestaurant.DAO.ECategoriaDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ECategoriaDAOImpl implements ECategoriaDAO {
    
    private final EntityManager em;
    
    public ECategoriaDAOImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public List<ECategoria> getAllCategories() throws Exception {
        try {
            TypedQuery<ECategoria> query = em.createQuery("SELECT c FROM ECategoria c", ECategoria.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero dei cuochi", e);
        }
    }
}
