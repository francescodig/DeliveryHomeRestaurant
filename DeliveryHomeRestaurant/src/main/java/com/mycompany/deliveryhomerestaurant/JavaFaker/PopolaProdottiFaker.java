/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.JavaFaker;

import com.github.javafaker.Faker;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;
import com.mycompany.deliveryhomerestaurant.Model.ECategoria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author simone
 */




public class PopolaProdottiFaker {

    public static void popola() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Faker faker = new Faker();
        Random random = new Random();

        
        List<ECategoria> categorie = em.createQuery("SELECT c FROM ECategoria c", ECategoria.class).getResultList();

        if (categorie.isEmpty()) {
            System.out.println("Popola prima le categorie nel DB.");
            em.close();
            emf.close();
            return;
        }

        Map<String, ECategoria> categoriaMap = new HashMap<>();
        for (ECategoria c : categorie) {
            categoriaMap.put(c.getNome(), c);
        }
        
        em.getTransaction().begin();
        
        em.createQuery("DELETE FROM EProdotto").executeUpdate();
        
        String[] nomiProdotti = {
            "Bruschette miste", "Lasagna alla bolognese", "Tagliata di manzo",
            "Patate al forno", "Tiramisù", "Acqua naturale", "Pizza Margherita",
            "Panino Classico", "Insalata Greca", "Couscous Vegano", "Parmigiana vegetariana",
            "Spaghetti alle vongole", "Bistecca alla griglia", "Cheesecake", "Birra artigianale",
            "Risotto ai funghi", "Pollo alla cacciatora", "Verdure grigliate", "Panna cotta",
            "Caffè espresso", "Calzone farcito", "Insalata Caesar", "Zuppa di pesce",
            "Hamburger di quinoa", "Gelato alla vaniglia"
        };

        String[] descrizioniProdotti = {
            "Pane tostato con pomodori, olive e aglio",
            "Pasta al forno con ragù e besciamella",
            "Carne di manzo servita con rucola e grana",
            "Contorno croccante e speziato",
            "Dolce al cucchiaio con mascarpone e caffè",
            "Bottiglia da 500ml",
            "Pomodoro, mozzarella e basilico",
            "Hamburger con insalata e formaggio",
            "Feta, olive, pomodorini e cipolla",
            "Piatto vegano con verdure e legumi",
            "Melanzane, sugo di pomodoro e formaggio",
            "Pasta con vongole fresche e prezzemolo",
            "Bistecca servita con contorno a scelta",
            "Dolce freddo con crema al formaggio",
            "Birra locale da 33cl",
            "Risotto cremoso con funghi porcini freschi",
            "Pollo cucinato con pomodoro, olive e rosmarino",
            "Mix di verdure grigliate stagionali",
            "Dolce al cucchiaio con panna e frutti di bosco",
            "Caffè italiano forte e aromatico",
            "Pizza ripiegata e farcita con salame e mozzarella",
            "Insalata con pollo, lattuga, crostini e parmigiano",
            "Zuppa ricca di pesce fresco e crostacei",
            "Hamburger vegetariano con quinoa e verdure",
            "Gelato artigianale alla vaniglia naturale"
        };
         
        Map<String, String> prodottoCategoriaMap = new HashMap<>();

        prodottoCategoriaMap.put("Bruschette miste", "Antipasti");
        prodottoCategoriaMap.put("Lasagna alla bolognese", "Primi Piatti");
        prodottoCategoriaMap.put("Tagliata di manzo", "Secondi Piatti");
        prodottoCategoriaMap.put("Patate al forno", "Contorni");
        prodottoCategoriaMap.put("Tiramisù", "Dolci");
        prodottoCategoriaMap.put("Acqua naturale", "Bevande");
        prodottoCategoriaMap.put("Pizza Margherita", "Pizze");
        prodottoCategoriaMap.put("Panino Classico", "Panini");
        prodottoCategoriaMap.put("Insalata Greca", "Insalate");
        prodottoCategoriaMap.put("Couscous Vegano", "Piatti Vegani");
        prodottoCategoriaMap.put("Parmigiana vegetariana", "Piatti Vegetariani");
        prodottoCategoriaMap.put("Spaghetti alle vongole", "Piatti di Mare");
        prodottoCategoriaMap.put("Bistecca alla griglia", "Piatti di Carne");
        prodottoCategoriaMap.put("Cheesecake", "Dessert");
        prodottoCategoriaMap.put("Birra artigianale", "Bibite Alcoliche");
        prodottoCategoriaMap.put("Risotto ai funghi", "Primi Piatti");
        prodottoCategoriaMap.put("Pollo alla cacciatora", "Secondi Piatti");
        prodottoCategoriaMap.put("Verdure grigliate", "Contorni");
        prodottoCategoriaMap.put("Panna cotta", "Dolci");
        prodottoCategoriaMap.put("Caffè espresso", "Bevande");
        prodottoCategoriaMap.put("Calzone farcito", "Pizze");
        prodottoCategoriaMap.put("Insalata Caesar", "Insalate");
        prodottoCategoriaMap.put("Zuppa di pesce", "Piatti di Mare");
        prodottoCategoriaMap.put("Hamburger di quinoa", "Piatti Vegani");
        prodottoCategoriaMap.put("Gelato alla vaniglia", "Dessert");


        for (int i = 0; i < nomiProdotti.length; i++) {
            EProdotto prodotto = new EProdotto();
            prodotto.setNome(nomiProdotti[i]);
            prodotto.setDescrizione(descrizioniProdotti[i]);
            prodotto.setCosto(BigDecimal.valueOf(5 + i).setScale(2));

            
            String nomeCategoria = prodottoCategoriaMap.get(nomiProdotti[i]);
            ECategoria categoria = categoriaMap.get(nomeCategoria);
            if (categoria == null) {
                System.out.println("Attenzione: categoria non trovata per prodotto: " + nomiProdotti[i]);
                continue;
            }
            prodotto.setCategoria(categoria);

            em.persist(prodotto);
        }

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Popolamento tabella prodotti completato.");
    }
}
