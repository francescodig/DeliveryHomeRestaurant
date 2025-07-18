/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

/**
 *
 * @author franc
 */
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//Crea le tabelle nel DB
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        System.out.println("EntityManagerFactory creato, DB e tabelle dovrebbero essere generate.");
        emf.close();
    }
}

