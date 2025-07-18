/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.ECalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EClienteDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EMenuDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EOrdineDao;
import com.mycompany.deliveryhomerestaurant.DAO.ERecensioneDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.mycompany.deliveryhomerestaurant.DAO.EProdottoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECartaCreditoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EIndirizzoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ECuocoDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EExceptionCalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ERiderDAO;
import com.mycompany.deliveryhomerestaurant.DAO.ESegnalazioneDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.Service.ProfiloService;
import com.mycompany.deliveryhomerestaurant.ServiceImpl.ProfiloServiceImpl;

// CREATO PER TESTARE I METODI DEL DAO 
public class Main {

    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        EOrdineDao ordineDAO = new EOrdineDAOImpl(em);
        EClienteDAO clienteDAO = new EClienteDAOImpl(em);
        ERecensioneDAO recensioneDAO  = new ERecensioneDAOImpl(em);
        EMenuDAO menuDAO = new EMenuDAOImpl(em);
        EProdottoDAO prodottoDAO = new EProdottoDAOImpl(em);
        ECartaCreditoDAO cartaDAO = new ECartaCreditoDAOImpl(em);
        EIndirizzoDAO indirizzoDAO = new EIndirizzoDAOImpl(em);
        ECuocoDAO cuocoDAO = new ECuocoDAOImpl(em);
        ERiderDAO riderDAO = new ERiderDAOImpl(em);
        ESegnalazioneDAO segnalazioneDAO = new ESegnalazioneDAOImpl(em);
        EUtenteDAO utenteDAO = new EUtenteDAOImpl(em);
        ProfiloService profileService = new ProfiloServiceImpl(utenteDAO);
        ECalendarioDAO calendarioDAO = new ECalendarioDAOImpl(em);
        EExceptionCalendarioDAO exceptioncalendarioDAO = new EExceptionCalendarioDAOImpl(em);


        /*
        
        // Testing per getAllOrders 
        
        List<EOrdine> tutti = ordineDAO.getAllOrders();
        
        for (EOrdine ordine : tutti) {
            System.out.println("ID: " + ordine.getId());
            System.out.println("Stato: " + ordine.getStato());
            System.out.println("Data esecuzione: " + ordine.getDataEsecuzione());
            System.out.println("Cliente: " + (ordine.getCliente() != null ? ordine.getCliente().getId() : "null"));
            // System.out.println("Indirizzo: " + (ordine.getIndirizzoConsegna() != null ? ordine.getIndirizzoConsegna().getId() : "null"));
            System.out.println("Prodotti ordinati: " + ordine.getProdotti().size());
            System.out.println("------");
        }
        
        */
        
        /* 
        
        // Testing per getOrdersByClient 
        
        ECliente cliente_x = clienteDAO.getClientById(19); 
        List<EOrdine> ordiniCliente = ordineDAO.getOrdersByClient(cliente_x);
        
        for (EOrdine ordine : ordiniCliente) {
            System.out.println("ID: " + ordine.getId());
            System.out.println("Stato: " + ordine.getStato());
            System.out.println("Data esecuzione: " + ordine.getDataEsecuzione());
            System.out.println("Cliente: " + (ordine.getCliente() != null ? ordine.getCliente().getId() : "null"));
            // System.out.println("Indirizzo: " + (ordine.getIndirizzoConsegna() != null ? ordine.getIndirizzoConsegna().getId() : "null"));
            System.out.println("Prodotti ordinati: " + ordine.getProdotti().size());
            System.out.println("------");
        }

        */
        
        /* 
        List<ERecensione> recensioni = recensioneDAO.getAllReviews();
        for (ERecensione recensione : recensioni){
            System.out.println("ID: " + recensione.getId());
            System.out.println("VOTO: " + recensione.getVoto());
            System.out.println("DESCRIZIONE: " + recensione.getDescrizione());
            System.out.println("CLIENTE: " + recensione.getCliente());
            System.out.println("DATA: " + recensione.getData());
            
            
            
            
        }
        */
        
        /*
        System.out.println("Recensione con ID 1:");
        ERecensione recensione = recensioneDAO.getReviewById(1);
        if (recensione != null) {
            System.out.println("ID: " + recensione.getId());
            System.out.println("Voto: " + recensione.getVoto());
            System.out.println("Descrizione: " + recensione.getDescrizione());
            System.out.println("Cliente ID: " + (recensione.getCliente() != null ? recensione.getCliente().getId() : "null"));
            System.out.println("Data: " + recensione.getData());
        } else {
            System.out.println("Recensione non trovata.");
        }
        */
        
        /*
        System.out.println("Recensioni del cliente con ID 19:");
        List<ERecensione> recensioniCliente = recensioneDAO.getReviewByClientId(19);
        for (ERecensione r : recensioniCliente) {
            System.out.println("ID: " + r.getId());
            System.out.println("Voto: " + r.getVoto());
            System.out.println("Descrizione: " + r.getDescrizione());
            System.out.println("Cliente ID: " + (r.getCliente() != null ? r.getCliente().getId() : "null"));
            System.out.println("Data: " + r.getData());
            System.out.println("------");
        }
        */
        

        

        /* 
        System.out.println("===== MENU COMPLETO =====");
        List<Map<String, Object>> menu = menuDAO.getMenu();

        for (Map<String, Object> categoria : menu) {
            System.out.println("Categoria: " + categoria.get("categoria"));
            List<Map<String, Object>> piatti = (List<Map<String, Object>>) categoria.get("piatti");

            for (Map<String, Object> piatto : piatti) {
                System.out.println("  ID: " + piatto.get("id"));
                System.out.println("  Nome: " + piatto.get("nome"));
                System.out.println("  Descrizione: " + piatto.get("descrizione"));
                System.out.println("  Costo: " + piatto.get("costo"));
                System.out.println("  ---");
            }
            System.out.println("=======");
        }
       */
        
       /*
        System.out.println("Lista di tutti i prodotti:");
        List<EProdotto> prodotti = prodottoDAO.getAllProducts();
        for (EProdotto p : prodotti) {
            System.out.println("ID: " + p.getId());
            System.out.println("Nome: " + p.getNome());
            System.out.println("Descrizione: " + p.getDescrizione());
            System.out.println("Costo: " + p.getCosto());
            System.out.println("------");
        }
       */
       
       
        // Tutte le carte di credito
        
        /*
        System.out.println("Tutte le carte:");
        List<ECartaCredito> carte = cartaDAO.getAllCreditCards();
        for (ECartaCredito c : carte) {
            System.out.println("Numero Carta: " + c.getNumeroCarta());
            System.out.println("Nome: " + c.getNomeCarta());
            System.out.println("Intestatario: " + c.getNomeIntestatario());
            System.out.println("Scadenza: " + c.getDataScadenza());
            System.out.println("------");
        }
        */
        

        
        // Carta per ID
        

        /*
        
        // Carta per ID utente
        int userId = 19;
        ECartaCredito cartaUtente = cartaDAO.getCreditCardByUserId(userId);
        if (cartaUtente != null) {
            System.out.println("Carta dell'utente " + userId + ": " + cartaUtente.getNumeroCarta());
        } else {
            System.out.println("Nessuna carta trovata per l'utente con ID " + userId);
        }
        
        */
        
        /*
        // 1. Tutti gli indirizzi
        System.out.println("Tutti gli indirizzi:");
        List<EIndirizzo> indirizzi = indirizzoDAO.getAllAddresses();
        for (EIndirizzo i : indirizzi) {
            System.out.println("ID: " + i.getId());
            System.out.println("Via: " + i.getVia());
            System.out.println("Città: " + i.getCitta());
            System.out.println("CAP: " + i.getCap());
            System.out.println("------");
        }
        */

        
//         //2. Indirizzo per ID
//
//        int indirizzoId = 1;
//        System.out.println("Indirizzo con ID " + indirizzoId + ":");
//        EIndirizzo indirizzo = indirizzoDAO.getAddressById(indirizzoId);
//        if (indirizzo != null) {
//            System.out.println("Via: " + indirizzo.getVia());
//            System.out.println("Città: " + indirizzo.getCitta());
//            System.out.println("CAP: " + indirizzo.getCap());
//        } else {
//            System.out.println("Nessun indirizzo trovato con ID " + indirizzoId);
//        }

        

        /*
        
         3. Indirizzo per ID utente
        int userIdIndirizzo = 17;
        EIndirizzo indirizzoUtente = indirizzoDAO.getAddressByUserId(userIdIndirizzo);
        System.out.println("Indirizzo dell'utente con ID " + userIdIndirizzo + ":");
        if (indirizzoUtente != null) {
            System.out.println("Via: " + indirizzoUtente.getVia());
            System.out.println("Città: " + indirizzoUtente.getCitta());
            System.out.println("CAP: " + indirizzoUtente.getCap());
        } else {
            System.out.println("Nessun indirizzo trovato per l'utente con ID " + userIdIndirizzo);
        }

        */
        
//        // === TEST getCartByClientId ===
//        int clientId = 19;
//        ECarrello carrello = carrelloDAO.getCartByClientId(clientId);
//
//        if (carrello != null) {
//            System.out.println("Carrello trovato per cliente ID " + clientId);
//            System.out.println("Carrello ID: " + carrello.getId());
//            System.out.println("Numero articoli nel carrello: " + carrello.getCarrelloItems().size());
//        } else {
//            System.out.println("Nessun carrello trovato per cliente ID " + clientId);
//        }
//
//        // === TEST addOrUpdateItemToCart ===
//        // Assumiamo che esista già un prodotto con ID 1
//        EProdotto prodotto = prodottoDAO.getProductById(1);
//        if (prodotto != null && carrello != null) {
//            EItemOrdine nuovoItem = new EItemOrdine();
//            nuovoItem.setProdotto(prodotto);
//            nuovoItem.setQuantita(10);
//            nuovoItem.setCarrello(carrello);
//            nuovoItem.setPrezzoUnitario(BigDecimal.ZERO);
//
//            
//            carrelloDAO.addOrUpdateItemToCart(carrello, nuovoItem);
//            
//            System.out.println("Prodotto aggiunto o aggiornato nel carrello.");
//        }

//        // === TEST removeOrUpdateItemFromCart ===
//        // Rimuove 1 quantità del prodotto con ID 1
//        if (prodotto != null && carrello != null) {
//            EItemOrdine itemToRemove = new EItemOrdine();
//            itemToRemove.setProdotto(prodotto);
//            itemToRemove.setQuantita(5);  // quantità da rimuovere
//
//            
//            carrelloDAO.removeOrUpdateItemFromCart(carrello.getId(), itemToRemove);
//           
//            System.out.println("Prodotto rimosso o quantità aggiornata nel carrello.");
//        }

//         //=== TEST getAllChefs ===
//        System.out.println("=== TUTTI I CUOCHI ===");
//        List<ECuoco> cuochi = cuocoDAO.getAllChefs();
//        for (ECuoco cuoco : cuochi) {
//            System.out.println("ID: " + cuoco.getId());
//            System.out.println("Nome: " + cuoco.getNome());
//            System.out.println("Cognome: " + cuoco.getCognome());
//            System.out.println("Email: " + cuoco.getEmail());
//            System.out.println("------");
//        }

//    // === TEST getAllRiders ===
//            System.out.println("=== TUTTI I RIDER ===");
//            List<ERider> riders = riderDAO.getAllRiders();
//            for (ERider rider : riders) {
//                System.out.println("ID: " + rider.getId());
//                System.out.println("Nome: " + rider.getNome());
//                System.out.println("Cognome: " + rider.getCognome());
//                System.out.println("Email: " + rider.getEmail());
//                System.out.println("------");
//            }

//            // Tutte le segnalazioni
//            System.out.println("=== TUTTE LE SEGNALAZIONI ===");
//            for (ESegnalazione s : segnalazioneDAO.getAllWarnings()) {
//                System.out.println("Segnalazione ID: " + s.getId());
//                System.out.println("Descrizione: " + s.getDescrizione());
//                System.out.println("------");
//            }
//
//            // Segnalazioni per cliente specifico (ad esempio id = 1)
//            System.out.println("=== SEGNALAZIONI PER CLIENTE 1 ===");
//            for (ESegnalazione s : segnalazioneDAO.getWarningsByClientId(1)) {
//                System.out.println("Segnalazione ID: " + s.getId());
//                System.out.println("Descrizione: " + s.getDescrizione());
//                System.out.println("------");
//            }


//            // === TEST REGISTRAZIONE UTENTE ===
//            ECliente nuovoCliente = new ECliente();
//            nuovoCliente.setNome("Mario");
//            nuovoCliente.setCognome("Rossi");
//            nuovoCliente.setEmail("mario.rossi@example.com");
//            nuovoCliente.setPassword("password123"); // Assicurati che la password venga gestita in modo sicuro nel DAO!
//
//            try {
//                profileService.Register(nuovoCliente);
//                System.out.println("Registrazione effettuata con successo per: " + nuovoCliente.getEmail());
//            } catch (Exception e) {
//                System.out.println("Errore nella registrazione: " + e.getMessage());
//            }



        
        em.close();
        emf.close();
        }
    
    
}



