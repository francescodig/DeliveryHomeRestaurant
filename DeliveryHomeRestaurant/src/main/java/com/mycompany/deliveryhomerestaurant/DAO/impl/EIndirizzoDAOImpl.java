/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


/**
 *
 * @author franc
 */


import java.util.List;

public class EIndirizzoDAOImpl implements EIndirizzoDAO {

    private final EntityManager entityManager;

    public EIndirizzoDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EIndirizzo> getAllAddresses() {
        TypedQuery<EIndirizzo> query = entityManager.createQuery("SELECT i FROM EIndirizzo i", EIndirizzo.class);
        return query.getResultList();
    }

    @Override
    public EIndirizzo getAddressById(int id) {
        return entityManager.find(EIndirizzo.class, id);
    }

    @Override
    public EIndirizzo getAddressByUserId(int userId) {
        TypedQuery<EIndirizzo> query = entityManager.createQuery(
            "SELECT i FROM EIndirizzo i JOIN i.clienti c WHERE c.id = :userId", EIndirizzo.class);
        query.setParameter("userId", userId);
        List<EIndirizzo> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}

