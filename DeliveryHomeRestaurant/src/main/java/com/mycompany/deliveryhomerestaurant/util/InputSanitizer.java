/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.util;

/**
 *
 * @author franc
 */
public class InputSanitizer {

    // Rimuove tag HTML/script
    public static String sanitize(String input) {
        if (input == null) return null;

        return input.replaceAll("<.*?>", "")
                    .replaceAll("[\"'<>]", ""); // rimuovi altri caratteri pericolosi
    }

}

