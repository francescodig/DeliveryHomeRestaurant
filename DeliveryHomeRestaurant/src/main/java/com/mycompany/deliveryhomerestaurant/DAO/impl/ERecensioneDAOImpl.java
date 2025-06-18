/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author franc
 */

public class ERecensioneDAOImpl implements ERecensioneDAO {

    private final EntityManager em;

    public ERecensioneDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ERecensione> getAllReviews() {
        return em.createQuery("SELECT r FROM ERecensione r", ERecensione.class).getResultList();
    }

    @Override
    public ERecensione getReviewById(int idRecensione) {
        return em.find(ERecensione.class, idRecensione);
    }

    @Override
    public List<ERecensione> getReviewByClientId(int idClient) {
        ECliente client = em.find(ECliente.class, idClient);
        if (client != null && client.getRecensioni() != null) {
            return new ArrayList<>(client.getRecensioni());
        } else {
            return new ArrayList<>();
        }
    }
}
