/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author franc
 */




@Entity
@Table(name = "carrello")
public class ECarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ECliente cliente;

    @OneToMany(mappedBy = "carrello", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<EItemCarrello> carrelloItems = new ArrayList<>();

    public ECarrello() {
    }

    // Getters

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public ECliente getCliente() {
        return cliente;
    }

    public List<EItemCarrello> getCarrelloItems() {
        return carrelloItems;
    }

    // Setters

    public void setCliente(ECliente cliente) {
        this.cliente = cliente;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}
