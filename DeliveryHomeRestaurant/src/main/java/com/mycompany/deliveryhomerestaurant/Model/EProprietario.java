/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;
import jakarta.persistence.*;

/**
 *
 * @author simone
 */
@Entity
@Table(name = "admin")
@DiscriminatorValue("admin")
public class EProprietario extends EUtente{
    @Column(name = "codice_proprietario", unique = true, nullable = false)
    private String codiceProprietario;

    public EProprietario() {
        super();
    }

    // Getter
    public String getCodiceProprietario() {
        return codiceProprietario;
    }

    // Setter
    public EProprietario setCodiceProprietario(String codiceProprietario) {
        this.codiceProprietario = codiceProprietario;
        return this;
    }
}
