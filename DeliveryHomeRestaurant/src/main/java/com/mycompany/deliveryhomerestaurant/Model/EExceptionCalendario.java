/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Model;

/**
 *
 * @author simone
 */
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "exception_calendario")
public class EExceptionCalendario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "exceptionDate", nullable = false)
    private LocalDate exceptionDate;

    @Column(name = "orario_apertura")
    private LocalTime orarioApertura;

    @Column(name = "orario_chiusura")
    private LocalTime orarioChiusura;

    @Column(nullable = false)
    private boolean aperto;

    @Column(name = "motivo_chiusura")
    private String motivoChiusura;

    public EExceptionCalendario() {
    }

    // Getter e Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(LocalDate exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public LocalTime getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(LocalTime orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public LocalTime getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(LocalTime orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public boolean isAperto() {
        return aperto;
    }

    public void setAperto(boolean aperto) {
        this.aperto = aperto;
    }

    public String getMotivoChiusura() {
        return motivoChiusura;
    }

    public void setMotivoChiusura(String motivoChiusura) {
        this.motivoChiusura = motivoChiusura;
    }
    
}
