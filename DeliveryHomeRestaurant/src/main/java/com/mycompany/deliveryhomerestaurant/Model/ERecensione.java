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
@Table(name = "recensione")
public class ERecensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Integer voto;

    @Column(nullable = false)
    private LocalDateTime data;

    // L'attributo orario non è definito in PHP, ma presente nei getter e setter: lo aggiungo come LocalTime
    @Column
    private java.time.LocalTime orario;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private EUtente cliente;

    public ERecensione() {
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Integer getVoto() {
        return voto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public java.time.LocalTime getOrario() {
        return orario;
    }

    public EUtente getCliente() {
        return cliente;
    }

    // Setters
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setOrario(java.time.LocalTime orario) {
        this.orario = orario;
    }

    public void setCliente(EUtente cliente) {
        this.cliente = cliente;
    }
}

