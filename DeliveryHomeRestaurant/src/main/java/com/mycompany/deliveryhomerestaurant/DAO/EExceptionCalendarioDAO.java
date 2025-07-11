/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.EExceptionCalendario;
import java.util.List;

/**
 *
 * @author simone
 */
public interface EExceptionCalendarioDAO {
    
    List<EExceptionCalendario> getExceptionCalendario() throws Exception;
    List<EExceptionCalendario> getGiorniChiusureStraordinarie() throws Exception;
    
}
