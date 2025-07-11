/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

/**
 *
 * @author simone
 */


import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.ECalendario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class PopolaCalendarioFaker {
    
     public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Random random = new Random();

        em.getTransaction().begin();

 
        for (int i = 0; i < 15; i++) {
            ECalendario calendario = new ECalendario();

            LocalDate data = LocalDate.now().plusDays(i); 
            calendario.setData(data);


            LocalTime apertura = LocalTime.of(random.nextInt(3) + 8, 0);  
            LocalTime chiusura = LocalTime.of(random.nextInt(4) + 18, 0); 

            calendario.setOrarioApertura(apertura);
            calendario.setOrarioChiusura(chiusura);

       
            calendario.setAperto(random.nextBoolean());

            em.persist(calendario);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella Calendario completato.");
    }
     
}
