package com.mycompany.deliveryhomerestaurant.JavaFaker;

import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.EIndirizzo;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Random;

public class PopolaIndirizziFaker {

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

        // Creiamo 20 indirizzi casuali
        for (int i = 0; i < 20; i++) {
            EIndirizzo indirizzo = new EIndirizzo();
            indirizzo.setVia(faker.address().streetName());
            indirizzo.setCivico(faker.address().buildingNumber());
            indirizzo.setCap(faker.address().zipCode().substring(0, 5)); // Adatta se necessario
            indirizzo.setCitta(faker.address().cityName());
            indirizzo.setAttivo(true);

            // Associa un cliente casuale all'indirizzo
            ECliente clienteCasuale = clienti.get(random.nextInt(clienti.size()));
            indirizzo.addCliente(clienteCasuale);

            em.persist(indirizzo);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        System.out.println("Popolamento tabella indirizzo completato con successo!");
    }
}
