/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

import jakarta.persistence.*;
import java.util.Set;
/**
 *
 * @author franc
 */

@Entity
@Table(name = "categoria")
public class ECategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private Set<EProdotto> piatti;

    @ManyToOne
    @JoinColumn(name = "elenco_prodotti_id", nullable = false)
    private EElencoProdotti elencoProdotti;

    public ECategoria() {
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

    public Set<EProdotto> getPiatti() {
        return piatti;
    }

    public void setPiatti(Set<EProdotto> piatti) {
        this.piatti = piatti;
    }

    public EElencoProdotti getElencoProdotti() {
        return elencoProdotti;
    }

    public void setElencoProdotti(EElencoProdotti elencoProdotti) {
        this.elencoProdotti = elencoProdotti;
    }
}
