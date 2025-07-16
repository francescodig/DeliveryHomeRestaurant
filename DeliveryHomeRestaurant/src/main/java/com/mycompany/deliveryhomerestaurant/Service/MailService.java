/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.Service;

import jakarta.mail.MessagingException;

/**
 *
 * @author franc
 */
public interface MailService {
    

    
    void sendEmail(String to, String subject, String text) throws MessagingException;
    
}
