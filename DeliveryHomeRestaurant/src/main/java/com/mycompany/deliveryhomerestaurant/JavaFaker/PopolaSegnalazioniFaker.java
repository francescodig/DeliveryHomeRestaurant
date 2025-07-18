/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author franc
 */

public class PopolaSegnalazioniFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        List<ECliente> clienti = em.createQuery("SELECT c FROM ECliente c", ECliente.class).getResultList();

        List<EOrdine> ordini = em.createQuery("SELECT o FROM EOrdine o", EOrdine.class).getResultList();

        if (clienti.isEmpty() || ordini.isEmpty()) {
            System.out.println("Non ci sono clienti o ordini nel DB: popola prima clienti e ordini.");
            em.close();
            emf.close();
            return;
        }

        em.getTransaction().begin();

        Set<EOrdine> ordiniUsati = new HashSet<>();

        
        for (int i = 0; i < 20 && ordiniUsati.size() < ordini.size(); i++) {
            ESegnalazione segnalazione = new ESegnalazione();

            segnalazione.setData(LocalDateTime.now()
                .minusDays(random.nextInt(30))
                .withHour(random.nextInt(24))
                .withMinute(random.nextInt(60)));

            segnalazione.setDescrizione(faker.lorem().sentence(4, 6));
            segnalazione.setTesto(faker.lorem().paragraph());

          
            EOrdine ordine;
            do {
                ordine = ordini.get(random.nextInt(ordini.size()));
            } while (ordiniUsati.contains(ordine));
            ordiniUsati.add(ordine);

            segnalazione.setOrdine(ordine);

            
            segnalazione.setCliente(clienti.get(random.nextInt(clienti.size())));

            em.persist(segnalazione);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        System.out.println("Popolamento tabella segnalazioni completato.");
    }
}
