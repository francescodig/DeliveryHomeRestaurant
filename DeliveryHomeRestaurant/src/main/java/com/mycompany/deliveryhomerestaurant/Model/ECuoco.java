/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;
import jakarta.persistence.*;

/**
 *
 * @author franc
 */
@Entity
@Table(name = "cuoco")
@DiscriminatorValue("cuoco")
public class ECuoco extends EUtente {

    @Column(name = "codice_cuoco", unique = true, nullable = false)
    private String codiceCuoco;

    public ECuoco() {
        super();
    }

    // Getter
    public String getCodiceCuoco() {
        return codiceCuoco;
    }

    // Setter
    public ECuoco setCodiceCuoco(String codiceCuoco) {
        this.codiceCuoco = codiceCuoco;
        return this;
    }
}
