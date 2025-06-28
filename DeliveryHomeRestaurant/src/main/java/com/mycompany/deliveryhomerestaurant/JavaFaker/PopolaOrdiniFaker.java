/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;


import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.EOrdine;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
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

            ECliente cliente = clienti.get(random.nextInt(clienti.size()));
            ordine.setCliente(cliente);

            // Note casuali (o null)
            if (random.nextBoolean()) {
                ordine.setNote(faker.lorem().sentence(3, 5));
            }

            // Date casuali
            LocalDateTime dataEsecuzione = LocalDateTime.now().minusDays(random.nextInt(10))
                    .withHour(faker.number().numberBetween(10, 22))
                    .withMinute(faker.number().numberBetween(0, 59));
            ordine.setDataEsecuzione(dataEsecuzione);

            ordine.setDataRicezione(dataEsecuzione.plusHours(random.nextInt(4)));
            
            // ---- Assegna indirizzo del cliente (ManyToMany) ----
            List<EIndirizzo> indirizzi = em.createQuery(
                    "SELECT i FROM EIndirizzo i JOIN i.clienti c WHERE c.id = :id", EIndirizzo.class)
                    .setParameter("id", cliente.getId())
                    .getResultList();
                if (indirizzi.isEmpty()) {
                    // Salta l’ordine se il cliente non ha indirizzi associati
                    continue;
                }

            EIndirizzo indirizzo = indirizzi.get(random.nextInt(indirizzi.size()));
            ordine.setIndirizzoConsegna(indirizzo);


            List<ECartaCredito> carte = em.createQuery("SELECT c FROM ECartaCredito c WHERE c.cliente.id = :id", ECartaCredito.class)
                .setParameter("id", cliente.getId())
                .getResultList();
            if (carte.isEmpty()) {
                System.out.println("Cliente " + cliente.getEmail() + " non ha carte di credito. Ordine saltato.");
                continue;
            }

        ECartaCredito carta = carte.get(random.nextInt(carte.size()));
        ordine.setCartaPagamento(carta);
            // ---- Data di consegna scelta dall'utente: tra 1 e 3 giorni dopo l'esecuzione ----
            LocalDateTime dataConsegna = dataEsecuzione.plusDays(faker.number().numberBetween(1, 4))
                    .withHour(faker.number().numberBetween(11, 21))
                    .withMinute(faker.number().numberBetween(0, 59));
            ordine.setDataConsegna(dataConsegna);

            // ---- Prodotti ----
            int numProdotti = faker.number().numberBetween(1, 6);
            Set<EProdotto> prodottiOrdine = new HashSet<>();
            for (int j = 0; j < numProdotti; j++) {
                prodottiOrdine.add(prodotti.get(random.nextInt(prodotti.size())));
            }
            ordine.setProdotti(prodottiOrdine);

            // ---- Costo ----
            BigDecimal costoTotale = prodottiOrdine.stream()
                    .map(p -> p.getCosto() != null ? p.getCosto() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ordine.setCosto(costoTotale);

            // ---- Stato casuale ----
            ordine.setStato(statiPossibili[random.nextInt(statiPossibili.length)]);

            em.persist(ordine);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella ordini completato.");
    }
}
