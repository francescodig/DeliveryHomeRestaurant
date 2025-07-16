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
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
@Table(name = "calendario")
public class ECalendario {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "data", nullable = false, unique = true)
    private DayOfWeek data;

    @Column(name = "orario_apertura")
    private LocalTime orarioApertura;

    @Column(name = "orario_chiusura")
    private LocalTime orarioChiusura;

    @Column(nullable = false)
    private boolean aperto;

    public ECalendario() {
    }

    // Getter e Setter

    public DayOfWeek getData() {
        return data;
    }

    public void setData(DayOfWeek data) {
        this.data = data;
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
}
