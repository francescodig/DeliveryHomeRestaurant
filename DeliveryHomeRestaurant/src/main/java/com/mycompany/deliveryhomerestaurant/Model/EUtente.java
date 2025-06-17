/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;


import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ruolo", discriminatorType = DiscriminatorType.STRING)
@Entity
@Table(name = "utente")
public abstract class EUtente <T extends EUtente<T>>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Costruttore
    public EUtente() {}

    // Getters
    public Integer getId() {
        return id;
    }


    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters

    public EUtente setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public EUtente setCognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public EUtente setEmail(String email) {
        this.email = email;
        return this;
    }

    public EUtente setPassword(String password) {
        // In Java, hash della password va fatto esternamente, qui settiamo la stringa già hashata
        this.password = password;
        return this;
    }

    // Metodo per ricavare il ruolo dal nome della sottoclasse
    public String getRuolo() {
        String className = this.getClass().getSimpleName(); // es: ECliente
        return className.startsWith("E") ? className.substring(1).toLowerCase() : className.toLowerCase();
    }
}

