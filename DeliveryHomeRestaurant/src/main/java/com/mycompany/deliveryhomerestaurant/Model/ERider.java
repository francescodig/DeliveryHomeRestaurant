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
@Table(name = "rider")
@DiscriminatorValue("rider")
public class ERider extends EUtente {

    @Column(name = "codice_rider", unique = true, nullable = false)
    private String codiceRider;

    public ERider() {
        super();
    }

    // Getter
    public String getCodiceRider() {
        return codiceRider;
    }

    // Setter
    public ERider setCodiceRider(String codiceRider) {
        this.codiceRider = codiceRider;
        return this;
    }
}
