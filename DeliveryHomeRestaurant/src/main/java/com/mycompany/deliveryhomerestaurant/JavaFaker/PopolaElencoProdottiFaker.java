    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package com.mycompany.deliveryhomerestaurant.JavaFaker;

    import com.github.javafaker.Faker;
    import com.mycompany.deliveryhomerestaurant.Model.EElencoProdotti;
    import com.mycompany.deliveryhomerestaurant.Model.ECategoria;

    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityManagerFactory;
    import jakarta.persistence.Persistence;
    /**
     *
     * @author franc
     */




    public class PopolaElencoProdottiFaker {

        public static void popola() {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
            EntityManager em = emf.createEntityManager();
            Faker faker = new Faker();

            em.getTransaction().begin();

            // Crea 5 elenchi prodotti
            for (int i = 0; i < 5; i++) {
                EElencoProdotti elenco = new EElencoProdotti();

                // Per ogni elenco, crea 3 categorie
                for (int j = 0; j < 3; j++) {
                    ECategoria categoria = new ECategoria();
                    categoria.setNome(faker.commerce().department()); // esempio: "Bakery"
                    elenco.addCategoria(categoria); // associazione bidirezionale
                }

                em.persist(elenco); // cascade persist su categorie
            }

            em.getTransaction().commit();
            em.close();
            emf.close();

            System.out.println("Popolamento elenco_prodotti completato con successo!");
        }
    }
