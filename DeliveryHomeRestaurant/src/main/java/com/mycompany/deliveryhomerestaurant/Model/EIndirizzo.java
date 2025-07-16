/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author franc
 */

@Entity
@Table(name = "indirizzo")
public class EIndirizzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private String civico;

    @Column(nullable = false)
    private String cap;

    @Column(nullable = false)
    private String citta;
    
    @Column(nullable = false)
    private boolean attivo;

    @ManyToMany(mappedBy = "indirizziConsegna")
    private Set<ECliente> clienti = new HashSet<>();
    
    @OneToMany(mappedBy = "indirizzoConsegna")
    private Set<EOrdine> ordini; 

    public EIndirizzo() {
        this.attivo = true;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
    
    public boolean getAttivo(){
        return this.attivo;
    }
    
    public void setAttivo(boolean attivo){
        this.attivo = attivo;
    }

    public Set<ECliente> getClienti() {
        return clienti;
    }

    public void setClienti(Set<ECliente> clienti) {
        this.clienti = clienti;
    }

    public void addCliente(ECliente cliente) {
        if (!clienti.contains(cliente)) {
            clienti.add(cliente);
            cliente.addIndirizzoConsegna(this);
        }
    }
}
