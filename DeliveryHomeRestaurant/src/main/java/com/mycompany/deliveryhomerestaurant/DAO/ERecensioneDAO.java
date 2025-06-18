/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.DAO;

import com.mycompany.deliveryhomerestaurant.Model.ERecensione;
import java.util.List;

/**
 *
 * @author franc
 */

public interface ERecensioneDAO {

    List<ERecensione> getAllReviews();

    ERecensione getReviewById(int idRecensione);

    List<ERecensione> getReviewByClientId(int idClient);
}
