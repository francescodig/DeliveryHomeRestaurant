/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

/**
 *
 * @author simone
 */


import com.mycompany.deliveryhomerestaurant.Model.ECalendario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Random;

public class PopolaCalendarioFaker {
    
    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Random random = new Random();

        em.getTransaction().begin();

        // Ciclo su tutti i giorni della settimana
        for (DayOfWeek giorno : DayOfWeek.values()) {
            // Controlla se il giorno esiste già, per evitare duplicati
            ECalendario esistente = em.find(ECalendario.class, giorno);
            if (esistente != null) {
                continue; // Salta se già presente
            }

            ECalendario calendario = new ECalendario();
            calendario.setData(giorno);

            // Genera orari casuali plausibili
            LocalTime apertura = LocalTime.of(random.nextInt(3) + 8, 0);  // tra 8:00 e 10:00
            LocalTime chiusura = LocalTime.of(random.nextInt(4) + 18, 0); // tra 18:00 e 21:00

            calendario.setOrarioApertura(apertura);
            calendario.setOrarioChiusura(chiusura);

            // Stato aperto o chiuso random (ma puoi anche farlo sempre aperto se vuoi)
            calendario.setAperto(random.nextBoolean());

            em.persist(calendario);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella Calendario completato.");
    }
}
