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
@Table(name = "elenco_prodotti")
public class EElencoProdotti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "elencoProdotti", cascade = CascadeType.PERSIST)
    private Set<ECategoria> categorie = new HashSet<>();

    public EElencoProdotti() {
    }

    // Getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<ECategoria> getCategorie() {
        return categorie;
    }

    public void setCategorie(Set<ECategoria> categorie) {
        this.categorie = categorie;
    }

    public void addCategoria(ECategoria categoria) {
        categorie.add(categoria);
        categoria.setElencoProdotti(this);
    }
}
