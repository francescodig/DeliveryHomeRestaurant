/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
/**
 *
 * @author franc
 */
@Entity
@Table(name = "prodotto")
public class EProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descrizione;
    
    @Column(nullable=false)
    private boolean attivo;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal costo;

    @ManyToMany(mappedBy = "prodotti")
    private Set<EOrdine> ordini;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private ECategoria categoria;

    public EProdotto() {
        this.attivo = true;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Set<EOrdine> getOrdini() {
        return ordini;
    }

    public void setOrdini(Set<EOrdine> ordini) {
        this.ordini = ordini;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }
    
    public boolean getAttivo(){
        return attivo;
    }
    
    public void setAttivo(boolean attivo){
        this.attivo = attivo;
    }
}
