/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;


import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author franc
 */



public class PopolaOrdiniFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        // Recupera clienti e prodotti già presenti
        List<ECliente> clienti = em.createQuery("SELECT c FROM ECliente c", ECliente.class).getResultList();
        List<EProdotto> prodotti = em.createQuery("SELECT p FROM EProdotto p", EProdotto.class).getResultList();

        if (clienti.isEmpty() || prodotti.isEmpty()) {
            System.out.println("Popola prima clienti e prodotti nel DB.");
            em.close();
            emf.close();
            return;
        }

        String[] statiPossibili = {"in_attesa", "in_preparazione", "pronto", "consegnato", "annullato"};

        em.getTransaction().begin();

        for (int i = 0; i < 20; i++) {
            EOrdine ordine = new EOrdine();

            ordine.setCliente(clienti.get(random.nextInt(clienti.size())));

            // Note casuali (o null)
            if (random.nextBoolean()) {
                ordine.setNote(faker.lorem().sentence(3, 5));
            }

            // Date casuali: data esecuzione tra 10 giorni fa e oggi, data ricezione >= data esecuzione
            LocalDateTime dataEsecuzione = LocalDateTime.now().minusDays(random.nextInt(10)).withHour(faker.number().numberBetween(10, 22)).withMinute(faker.number().numberBetween(0, 59));
            ordine.setDataEsecuzione(dataEsecuzione);
            // Data ricezione da 0 a 3 ore dopo esecuzione
            ordine.setDataRicezione(dataEsecuzione.plusHours(random.nextInt(4)));

            // Prodotti casuali (tra 1 e 5 prodotti)
            int numProdotti = faker.number().numberBetween(1, 6);
            Set<EProdotto> prodottiOrdine = new HashSet<>();
            for (int j = 0; j < numProdotti; j++) {
                prodottiOrdine.add(prodotti.get(random.nextInt(prodotti.size())));
            }
            ordine.setProdotti(prodottiOrdine);

            // Calcolo costo sommando i prezzi prodotti (ipotizzando EProdotto abbia metodo getPrezzo())
            BigDecimal costoTotale = prodottiOrdine.stream()
                    .map(p -> p.getCosto() != null ? p.getCosto() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            ordine.setCosto(costoTotale);

            // Stato casuale
            ordine.setStato(statiPossibili[random.nextInt(statiPossibili.length)]);

            em.persist(ordine);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella ordini completato.");
    }
}
