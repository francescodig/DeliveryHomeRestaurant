/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author simone
 */

import com.mycompany.deliveryhomerestaurant.Model.EProprietario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Scanner;

public class create_admin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci email: ");
        String email = scanner.nextLine();

        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        scanner.close();

        // Hash password con BCrypt
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());

        // Crea EProprietario
        EProprietario admin = new EProprietario();
        admin.setEmail(email);
        admin.setPassword(hashedPassword);
        admin.setNome("Admin");
        admin.setCognome("Admin");
        admin.setCodiceProprietario("1");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Controlla se utente già esiste
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM EProprietario u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();

            if (count > 0) {
                System.out.println("Utente già esistente con questa email.");
                System.exit(1);
            }

            em.persist(admin);
            em.getTransaction().commit();
            System.out.println("✅ Admin creato con successo con email: " + email);

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            System.out.println("Errore durante la creazione dell'admin: " + e.getMessage());
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
    
}
