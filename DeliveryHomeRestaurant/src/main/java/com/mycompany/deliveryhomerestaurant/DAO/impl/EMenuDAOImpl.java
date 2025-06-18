/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import jakarta.persistence.EntityManager;

import java.util.*;

/**
 *
 * @author franc
 */




public class EMenuDAOImpl implements EMenuDAO {

    private final EntityManager em;

    public EMenuDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Map<String, Object>> getMenu() {
        List<ECategoria> categorie = em.createQuery("SELECT c FROM ECategoria c", ECategoria.class).getResultList();
        List<Map<String, Object>> menu = new ArrayList<>();

        for (ECategoria cat : categorie) {
            Map<String, Object> categoriaMap = new HashMap<>();
            categoriaMap.put("categoria", cat.getNome());

            List<Map<String, Object>> piattiList = new ArrayList<>();
            for (EProdotto piatto : cat.getPiatti()) {
                Map<String, Object> piattoMap = new HashMap<>();
                piattoMap.put("id", piatto.getId());
                piattoMap.put("nome", piatto.getNome());
                piattoMap.put("descrizione", piatto.getDescrizione());
                piattoMap.put("costo", piatto.getCosto());

                piattiList.add(piattoMap);
            }

            categoriaMap.put("piatti", piattiList);
            menu.add(categoriaMap);
        }

        return menu;
    }
}
