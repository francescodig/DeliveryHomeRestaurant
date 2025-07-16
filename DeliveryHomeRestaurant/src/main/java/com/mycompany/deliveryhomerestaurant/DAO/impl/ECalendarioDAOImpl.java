/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

/**
 *
 * @author simone
 */
import com.mycompany.deliveryhomerestaurant.DAO.ECalendarioDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECalendario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.DayOfWeek;
import java.util.List;

public class ECalendarioDAOImpl implements ECalendarioDAO{
    
    private final EntityManager em;
    
    public ECalendarioDAOImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public List<ECalendario> getCalendario() throws Exception {
        try {
            TypedQuery<ECalendario> query = em.createQuery("SELECT c FROM ECalendario c", ECalendario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero del calendario", e);
        }
    }
    
    @Override
    public ECalendario getDayById(DayOfWeek data){
        return em.find(ECalendario.class, data);
    }
    
    @Override
    public List<ECalendario> getGiorniChiusureSettimanali() throws Exception {
        try {
            TypedQuery<ECalendario> query = em.createQuery("SELECT c FROM ECalendario c WHERE c.aperto = false", ECalendario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero del calendario", e);
        }
    }
    
    @Override
    public List<ECalendario> getGiorniApertureSettimanali() throws Exception {
        try {
            TypedQuery<ECalendario> query = em.createQuery("SELECT c FROM ECalendario c WHERE c.aperto = true", ECalendario.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception("Errore durante il recupero del calendario", e);
        }
    }
    
}
