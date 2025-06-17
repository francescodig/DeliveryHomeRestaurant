/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;


import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
/**
 *
 * @author franc
 */



public class PopolaRecensioniFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        // Recupera tutti gli utenti (o solo clienti se vuoi filtrare)
        List<EUtente> utenti = em.createQuery("SELECT u FROM EUtente u", EUtente.class).getResultList();

        if (utenti.isEmpty()) {
            System.out.println("Non ci sono utenti nel DB: popola prima gli utenti.");
            em.close();
            emf.close();
            return;
        }

        em.getTransaction().begin();

        for (int i = 0; i < 30; i++) { // crea 30 recensioni casuali
            ERecensione recensione = new ERecensione();

            recensione.setDescrizione(faker.lorem().sentence(10, 15));
            recensione.setVoto(random.nextInt(5) + 1); // voto da 1 a 5

            // Data e ora casuale recente (negli ultimi 30 giorni)
            LocalDateTime data = LocalDateTime.now().minusDays(random.nextInt(30)).withHour(random.nextInt(24)).withMinute(random.nextInt(60));
            recensione.setData(data);
            recensione.setOrario(LocalTime.of(data.getHour(), data.getMinute()));

            // Associa un utente casuale
            recensione.setCliente(utenti.get(random.nextInt(utenti.size())));

            em.persist(recensione);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella recensioni completato.");
    }
}
