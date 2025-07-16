package com.mycompany.deliveryhomerestaurant.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {
    public static final String API_KEY;
    public static final String INDIRIZZO_RISTORANTE = "Piazza Duomo 1, L'Aquila";;
    public static final String smtpHost = "smtp.gmail.com";
    public static final int port = 587; 
    public static final String email = "delivery.home.restaurantaq@gmail.com";
    public static final String passwordApp = "uvjhgacwkxabqmtm";

    static {
        Properties prop = new Properties();
        try (InputStream input = Constants.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);
            API_KEY = prop.getProperty("api.key");
        } catch (IOException e) {
            throw new RuntimeException("API_KEY non trovata");
        }
    }
}
