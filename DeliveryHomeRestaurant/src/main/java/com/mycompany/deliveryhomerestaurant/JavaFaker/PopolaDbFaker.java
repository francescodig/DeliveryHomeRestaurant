/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
/**
 *
 * @author franc
 */



public class PopolaDbFaker {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            PopolaUtentiFaker.popola(); 
            PopolaElencoProdottiFaker.popola();
            PopolaCategorieFaker.popola();
            PopolaProdottiFaker.popola();
            PopolaCarteCreditoFaker.popola();
            PopolaIndirizziFaker.popola(); 
            PopolaOrdiniFaker.popola();
            PopolaRecensioniFaker.popola();
            PopolaSegnalazioniFaker.popola();
            PopolaCalendarioFaker.popola();
            PopolaExceptionCalendarioFaker.popola();
            
           
            
            
            
            
            em.getTransaction().commit();

            System.out.println("Popolamento database completato con successo!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}