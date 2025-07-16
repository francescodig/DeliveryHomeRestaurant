/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;


import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;
import com.mycompany.deliveryhomerestaurant.Model.EElencoProdotti;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Random;

/**
 *
 * @author simone
 */



public class PopolaCategorieFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        // Recupera gli elenchi prodotti esistenti
        List<EElencoProdotti> elenchiProdotti = em.createQuery("SELECT e FROM EElencoProdotti e", EElencoProdotti.class).getResultList();

        if (elenchiProdotti.isEmpty()) {
            System.out.println("Popola prima gli EElencoProdotti nel DB.");
            em.close();
            emf.close();
            return;
        }
        
         em.getTransaction().begin();
         
        em.createQuery("DELETE FROM ECategoria").executeUpdate();
        
        List<String> nomiCategorie = List.of(
            "Antipasti",
            "Primi Piatti",
            "Secondi Piatti",
            "Contorni",
            "Dolci",
            "Bevande",
            "Pizze",
            "Panini",
            "Insalate",
            "Piatti Vegani",
            "Piatti Vegetariani",
            "Piatti di Mare",
            "Piatti di Carne",
            "Dessert",
            "Bibite Alcoliche"
        );

        
        int numeroCategorie = Math.min(nomiCategorie.size(), 15);

        for (int i = 0; i < numeroCategorie; i++) {
            ECategoria categoria = new ECategoria();

            categoria.setNome(nomiCategorie.get(i));

            // Associa un elenco prodotti casuale
            categoria.setElencoProdotti(elenchiProdotti.get(random.nextInt(elenchiProdotti.size())));

            em.persist(categoria);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella categorie completato.");
    }
}

