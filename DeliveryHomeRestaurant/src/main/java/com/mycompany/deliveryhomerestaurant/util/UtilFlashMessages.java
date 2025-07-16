/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuli
 */
public class UtilFlashMessages {
    private static final String FLASH_KEY = "flash";

    public static void addMessage(HttpServletRequest request, String type, String message) {
        HttpSession session = UtilSession.getSession(request);
        if (session == null) return;

        Map<String, List<String>> messages = (Map<String, List<String>>) session.getAttribute(FLASH_KEY);
        if (messages == null) {
            messages = new HashMap<>();
        }

        messages.computeIfAbsent(type, k -> new ArrayList<>()).add(message);
        session.setAttribute(FLASH_KEY, messages);
    }

    public static Map<String, List<String>> getMessage(HttpServletRequest request) {
        HttpSession session = UtilSession.getSession(request);
        if (session == null) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> messages = (Map<String, List<String>>) session.getAttribute(FLASH_KEY);
        if (messages == null) {
            return Collections.emptyMap();
        }

        session.removeAttribute(FLASH_KEY);
        return messages;
    }

    public static boolean hasMessage(HttpServletRequest request) {
        HttpSession session = UtilSession.getSession(request);
        return session != null && session.getAttribute(FLASH_KEY) != null;
    }
}
