/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.ESegnalazione;
import java.util.List;

/**
 *
 * @author franc
 */

//NON IMPLEMENTATA ALLA FINE 
public interface ESegnalazioneDAO {
    List<ESegnalazione> getAllWarnings();
    List<ESegnalazione> getWarningsByClientId(int clientId);
}
