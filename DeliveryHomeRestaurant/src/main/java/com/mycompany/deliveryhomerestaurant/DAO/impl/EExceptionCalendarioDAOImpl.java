/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

/**
 *
 * @author simone
 */
import com.mycompany.deliveryhomerestaurant.DAO.EExceptionCalendarioDAO;
import com.mycompany.deliveryhomerestaurant.Model.EExceptionCalendario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EExceptionCalendarioDAOImpl implements EExceptionCalendarioDAO{
    
    private final EntityManager em;
    
    public EExceptionCalendarioDAOImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public List<EExceptionCalendario> getExceptionCalendario() throws Exception {
        try {
            TypedQuery<EExceptionCalendario> query = em.createQuery("SELECT e FROM EExceptionCalendario e", EExceptionCalendario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero del calendario", e);
        }
    }
    
    @Override
    public List<EExceptionCalendario> getGiorniChiusureStraordinarie() throws Exception {
        try {
            TypedQuery<EExceptionCalendario> query = em.createQuery("SELECT e FROM EExceptionCalendario e WHERE e.aperto = false", EExceptionCalendario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero del calendario", e);
        }
    }
}
