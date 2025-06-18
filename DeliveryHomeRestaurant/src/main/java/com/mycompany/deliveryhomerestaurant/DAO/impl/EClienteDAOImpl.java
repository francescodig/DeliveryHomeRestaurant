/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
/**
 *
 * @author franc
 */


import java.util.List;

public class EClienteDAOImpl implements EClienteDAO {

    private final EntityManager em;

    public EClienteDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean verify(String field, Object value) {
        String jpql = "SELECT COUNT(u) FROM EUser u WHERE u." + field + " = :value";
        Long count = em.createQuery(jpql, Long.class)
                       .setParameter("value", value)
                       .getSingleResult();
        return count > 0;
    }

    @Override
    public List<ECliente> getAllClients() {
        TypedQuery<ECliente> query = em.createQuery("SELECT u FROM EUser u", ECliente.class);
        return query.getResultList();
    }

    @Override
    public ECliente getClientById(int userId) {
        return em.find(ECliente.class, userId);
    }
}
