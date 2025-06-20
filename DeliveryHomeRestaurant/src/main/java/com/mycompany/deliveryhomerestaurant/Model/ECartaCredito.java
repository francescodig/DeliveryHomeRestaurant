/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author franc
 */

@Entity
@Table(name = "carta_credito")
public class ECartaCredito {

    @Id
    @Column(name = "numeroCarta", nullable = false, unique = true)
    private String numeroCarta;

    @Column(name = "nomeCarta", nullable = false)
    private String nomeCarta;

    @Column(name = "dataScadenza", nullable = false)
    private LocalDateTime dataScadenza;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Column(name = "nomeIntestatario", nullable = false)
    private String nomeIntestatario;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private ECliente cliente;
    
    @OneToMany(mappedBy = "cartaPagamento")
    private Set<EOrdine> ordini;


    public ECartaCredito() {
    }

    // Getters and setters

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nomeCarta) {
        this.nomeCarta = nomeCarta;
    }

    public LocalDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNomeIntestatario() {
        return nomeIntestatario;
    }

    public void setNomeIntestatario(String nomeIntestatario) {
        this.nomeIntestatario = nomeIntestatario;
    }

    public ECliente getCliente() {
        return cliente;
    }

    public void setCliente(ECliente cliente) {
        this.cliente = cliente;
    }

}

