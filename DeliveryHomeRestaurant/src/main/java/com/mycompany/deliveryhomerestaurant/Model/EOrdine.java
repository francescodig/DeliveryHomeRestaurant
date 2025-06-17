package com.mycompany.deliveryhomerestaurant.Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
/**
 *
 * @author franc
 */


@Entity
@Table(name = "ordine")
public class EOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String note;

    @Column(name = "data_esecuzione")
    private LocalDateTime dataEsecuzione;

    @Column(name = "data_ricezione")
    private LocalDateTime dataRicezione;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private ECliente cliente;

    @OneToOne(mappedBy = "ordine")
    private ESegnalazione segnalazione;

    @ManyToMany
    @JoinTable(name = "ordini_prodotti",
        joinColumns = @JoinColumn(name = "ordine_id"),
        inverseJoinColumns = @JoinColumn(name = "prodotto_id"))
    private Set<EProdotto> prodotti;

    @Column(columnDefinition = "ENUM('in_attesa', 'in_preparazione', 'pronto', 'consegnato', 'annullato')")
    private String stato;

    public EOrdine() {
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDataEsecuzione() {
        return dataEsecuzione;
    }

    public void setDataEsecuzione(LocalDateTime dataEsecuzione) {
        this.dataEsecuzione = dataEsecuzione;
    }

    public LocalDateTime getDataRicezione() {
        return dataRicezione;
    }

    public void setDataRicezione(LocalDateTime dataRicezione) {
        this.dataRicezione = dataRicezione;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public ECliente getCliente() {
        return cliente;
    }

    public void setCliente(ECliente cliente) {
        this.cliente = cliente;
    }

    public ESegnalazione getSegnalazione() {
        return segnalazione;
    }

    public void setSegnalazione(ESegnalazione segnalazione) {
        this.segnalazione = segnalazione;
    }

    public Set<EProdotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(Set<EProdotto> prodotti) {
        this.prodotti = prodotti;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
