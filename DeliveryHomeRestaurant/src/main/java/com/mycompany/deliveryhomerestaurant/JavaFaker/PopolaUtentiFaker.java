/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;
import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECliente;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import com.mycompany.deliveryhomerestaurant.Model.ERider;
import com.mycompany.deliveryhomerestaurant.Model.ECuoco;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/**
 *
 * @author franc
 */

public class PopolaUtentiFaker {

    public static void popola() {
        // Crea EntityManager (usa il nome della tua persistence unit)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();

        em.getTransaction().begin();

        for (int i = 0; i < 20; i++) {
            ECliente cliente = (ECliente) new ECliente()
                .setNome(faker.name().firstName())
                .setCognome(faker.name().lastName())
                .setEmail(faker.internet().emailAddress())
                // Simula una password hashata (ad esempio md5 simulato con faker random alfanumerico)
                .setPassword(faker.internet().password(8, 16, true, true, true));

            em.persist(cliente);
        }

        em.getTransaction().commit();
        
        em.getTransaction().begin();

        for (int i = 0; i < 20; i++) {
            ERider rider = (ERider) new ERider()
                .setCodiceRider("RID-" + faker.number().digits(5))
                .setNome(faker.name().firstName())
                .setCognome(faker.name().lastName())
                .setEmail(faker.internet().emailAddress())
                // Simula una password hashata (ad esempio md5 simulato con faker random alfanumerico)
                .setPassword(faker.internet().password(8, 16, true, true, true));  
            
            

            em.persist(rider);
        }

        em.getTransaction().commit();
        
        em.getTransaction().begin();

        for (int i = 0; i < 20; i++) {
            ECuoco cuoco = (ECuoco) new ECuoco()
                .setCodiceCuoco("CUO-" + faker.number().digits(5))
                .setNome(faker.name().firstName())
                .setCognome(faker.name().lastName())
                .setEmail(faker.internet().emailAddress())
                // Simula una password hashata (ad esempio md5 simulato con faker random alfanumerico)
                .setPassword(faker.internet().password(8, 16, true, true, true));

            em.persist(cuoco);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento DB con utenti Faker completato.");
    }
}
