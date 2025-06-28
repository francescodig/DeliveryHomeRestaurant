/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class EOrdineDAOImpl implements EOrdineDao {

    private final EntityManager em;

    public EOrdineDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public EOrdine getOrdineById(String id){
        
        try {
            return em.find(EOrdine.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    @Override
    public List<EOrdine> getAllOrders() {
        TypedQuery<EOrdine> query = em.createQuery("SELECT o FROM EOrdine o", EOrdine.class);
        return query.getResultList();
    }

    @Override
    public List<EOrdine> getOrdersByClient(ECliente cliente) {
        TypedQuery<EOrdine> query = em.createQuery(
            "SELECT o FROM EOrdine o WHERE o.cliente = :cliente", EOrdine.class);
        query.setParameter("cliente", cliente);
        return query.getResultList();
    }

    @Override
    public List<EOrdine> getOrdersByState(String stato) {
        TypedQuery<EOrdine> query = em.createQuery(
            "SELECT o FROM EOrdine o WHERE o.stato = :stato", EOrdine.class);
        query.setParameter("stato", stato);
        return query.getResultList();
    }

    @Override
    public List<EOrdine> getOrdersByDataEsecuzione(LocalDateTime data) {
        TypedQuery<EOrdine> query = em.createQuery(
            "SELECT o FROM EOrdine o WHERE o.dataEsecuzione = :data", EOrdine.class);
        query.setParameter("data", data);
        return query.getResultList();
    }

    @Override
    public List<EOrdine> getOrdersByDataGiornaliera(LocalDateTime data) {
        LocalDateTime inizioGiorno = data.toLocalDate().atStartOfDay();
        LocalDateTime fineGiorno = data.toLocalDate().atTime(23, 59, 59);
        TypedQuery<EOrdine> query = em.createQuery(
            "SELECT o FROM EOrdine o WHERE o.dataEsecuzione BETWEEN :inizio AND :fine", EOrdine.class);
        query.setParameter("inizio", inizioGiorno);
        query.setParameter("fine", fineGiorno);
        return query.getResultList();
    }
}
