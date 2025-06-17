/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author franc
 */

@Entity
@Table(name = "segnalazione")
public class ESegnalazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String testo;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private ECliente cliente;

    @OneToOne
    @JoinColumn(name = "ordine_id", nullable = false)
    private EOrdine ordine;

    public ESegnalazione() {
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTesto() {
        return testo;
    }

    public ECliente getCliente() {
        return cliente;
    }

    public EOrdine getOrdine() {
        return ordine;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setCliente(ECliente cliente) {
        this.cliente = cliente;
    }

    public void setOrdine(EOrdine ordine) {
        this.ordine = ordine;
    }
}
