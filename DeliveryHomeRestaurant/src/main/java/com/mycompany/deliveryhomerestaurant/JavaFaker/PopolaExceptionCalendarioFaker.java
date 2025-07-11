/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

/**
 *
 * @author simone
 */

import com.mycompany.deliveryhomerestaurant.Model.EExceptionCalendario;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class PopolaExceptionCalendarioFaker {
    
    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        em.getTransaction().begin();

        
        for (int i = 0; i < 10; i++) {
            EExceptionCalendario exception = new EExceptionCalendario();

         
            LocalDate date = LocalDate.now().plusDays(random.nextInt(60));
            exception.setExceptionDate(date);

           
            LocalTime apertura = LocalTime.of(random.nextInt(3) + 8, 0); 
            LocalTime chiusura = LocalTime.of(random.nextInt(4) + 18, 0);
            exception.setOrarioApertura(apertura);
            exception.setOrarioChiusura(chiusura);

    
            exception.setAperto(false);

       
            String motivo = faker.lorem().sentence(3, 5);
            exception.setMotivoChiusura(motivo);

            em.persist(exception);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella ExceptionCalendario completato.");
    }
    
}
