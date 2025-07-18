/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECartaCredito;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 *
 * @author franc
 */

public class PopolaCarteCreditoFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        
        List<ECliente> clienti = em.createQuery("SELECT c FROM ECliente c", ECliente.class).getResultList();

        if (clienti.isEmpty()) {
            System.out.println("Non ci sono clienti nel database. Popola prima i clienti.");
            em.close();
            emf.close();
            return;
        }

        em.getTransaction().begin();

        // Creiamo 20 carte di credito casuali
        for (int i = 0; i < 20; i++) {
            ECartaCredito carta = new ECartaCredito();

            String numeroCarta = faker.finance().creditCard().replaceAll("-", "");
            carta.setNumeroCarta(numeroCarta);

            carta.setNomeCarta("VISA");

            // Genera una data di scadenza futura tra 1 e 5 anni da oggi
            LocalDateTime dataScadenza = LocalDateTime.now().plusMonths(12 + random.nextInt(48));
            carta.setDataScadenza(dataScadenza);

            carta.setCvv(String.format("%03d", random.nextInt(1000)));

            carta.setNomeIntestatario(faker.name().fullName());

            carta.setCliente(clienti.get(random.nextInt(clienti.size())));
            
            carta.setAttivo(true);

            em.persist(carta);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        System.out.println("Popolamento tabella carta_credito completato con successo!");
    }
}
