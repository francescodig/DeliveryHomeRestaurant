/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.ECalendario;
import java.util.List;

/**
 *
 * @author simone
 */
public interface ECalendarioDAO {
    
    List<ECalendario> getCalendario() throws Exception;
    List<ECalendario> getGiorniChiusureSettimanali() throws Exception;
    ECalendario getDayById(String id);
    List<ECalendario> getGiorniApertureSettimanali() throws Exception;
    
}
