/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@DiscriminatorValue("cliente")
public class ECliente extends EUtente {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ECartaCredito> metodiPagamento = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "clienti_indirizzi",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "indirizzo_id")
    )
    private List<EIndirizzo> indirizziConsegna = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EOrdine> ordini = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ESegnalazione> segnalazioni = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ERecensione> recensioni = new ArrayList<>();



    public ECliente() {
        super();
    }

    // Getters e Setters

    public List<ECartaCredito> getMetodiPagamento() {
        return metodiPagamento;
    }

    public void setMetodiPagamento(List<ECartaCredito> metodiPagamento) {
        this.metodiPagamento = metodiPagamento;
    }

    public List<EIndirizzo> getIndirizziConsegna() {
        return indirizziConsegna;
    }

    public void setIndirizziConsegna(List<EIndirizzo> indirizziConsegna) {
        this.indirizziConsegna = indirizziConsegna;
    }

    public void addIndirizzoConsegna(EIndirizzo indirizzo) {
        this.indirizziConsegna.add(indirizzo);
    }

    public List<EOrdine> getOrdini() {
        return ordini;
    }

    public void setOrdini(List<EOrdine> ordini) {
        this.ordini = ordini;
    }

    public List<ESegnalazione> getSegnalazioni() {
        return segnalazioni;
    }

    public void setSegnalazioni(List<ESegnalazione> segnalazioni) {
        this.segnalazioni = segnalazioni;
    }

    public List<ERecensione> getRecensioni() {
        return recensioni;
    }

    public void setRecensioni(List<ERecensione> recensioni) {
        this.recensioni = recensioni;
    }




}
