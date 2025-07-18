/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.ESegnalazioneDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author franc
 */


//NON IMPLEMENTATA ALLA FINE 
public class ESegnalazioneDAOImpl implements ESegnalazioneDAO {

    private EntityManager em;

    public ESegnalazioneDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ESegnalazione> getAllWarnings() {
        return em.createQuery("SELECT s FROM ESegnalazione s", ESegnalazione.class).getResultList();
    }

    @Override
    public List<ESegnalazione> getWarningsByClientId(int clientId) {
        ECliente cliente = em.find(ECliente.class, clientId);
        if (cliente != null) {
            return cliente.getSegnalazioni(); 
        }
        return List.of(); 
    }
}
