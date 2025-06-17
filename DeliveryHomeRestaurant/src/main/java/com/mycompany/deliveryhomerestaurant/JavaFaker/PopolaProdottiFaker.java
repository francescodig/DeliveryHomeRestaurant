/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 *
 * @author franc
 */




public class PopolaProdottiFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        // Recupera le categorie esistenti
        List<ECategoria> categorie = em.createQuery("SELECT c FROM ECategoria c", ECategoria.class).getResultList();

        if (categorie.isEmpty()) {
            System.out.println("Popola prima le categorie nel DB.");
            em.close();
            emf.close();
            return;
        }

        em.getTransaction().begin();

        for (int i = 0; i < 30; i++) {
            EProdotto prodotto = new EProdotto();

            // Nome e descrizione
            prodotto.setNome(faker.food().ingredient());
            prodotto.setDescrizione(faker.lorem().sentence(5, 8));

            // Costo casuale da 1.00 a 50.00 euro, con due decimali
            double prezzo = 1 + (50 - 1) * random.nextDouble();
            prodotto.setCosto(BigDecimal.valueOf(prezzo).setScale(2, BigDecimal.ROUND_HALF_UP));

            // Categoria casuale
            prodotto.setCategoria(categorie.get(random.nextInt(categorie.size())));

            em.persist(prodotto);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella prodotti completato.");
    }
}
