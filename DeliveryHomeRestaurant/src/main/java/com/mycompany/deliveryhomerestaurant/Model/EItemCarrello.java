/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
/**
 *
 * @author franc
 */

@Entity
@Table(name = "item_carrello")
public class EItemCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "carrello_id", nullable = false)
    private ECarrello carrello;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private EProdotto prodotto;

    @Column(nullable = false)
    private Integer quantita;

    @Column(name = "prezzo_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal prezzoUnitario;

    public EItemCarrello() {
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public ECarrello getCarrello() {
        return carrello;
    }

    public EProdotto getProdotto() {
        return prodotto;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public BigDecimal getPrezzoTotale() {
        return prezzoUnitario.multiply(new BigDecimal(quantita));
    }

    // Setters
    public void setCarrello(ECarrello carrello) {
        this.carrello = carrello;
    }

    public void setProdotto(EProdotto prodotto) {
        this.prodotto = prodotto;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }
}

