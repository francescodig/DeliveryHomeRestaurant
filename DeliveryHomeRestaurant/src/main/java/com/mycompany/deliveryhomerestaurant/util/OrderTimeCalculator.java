/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.util;

/**
 *
 * @author franc
 */


import com.mycompany.deliveryhomerestaurant.Model.EItemOrdine;
import com.mycompany.deliveryhomerestaurant.Model.EProdotto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class OrderTimeCalculator {

    private final Map<String, Integer> tempiPerCategoria = new HashMap<>();
    private final String indirizzoRistorante;
    private final String apiKey;

    public OrderTimeCalculator() {
        this.apiKey = Constants.API_KEY;
        this.indirizzoRistorante = Constants.INDIRIZZO_RISTORANTE;

        tempiPerCategoria.put("Antipasti", 5);
        tempiPerCategoria.put("Primi", 10);
        tempiPerCategoria.put("Secondi", 10);
        tempiPerCategoria.put("Dolci", 5);
        tempiPerCategoria.put("Bevande", 0);
    }

    public int timeCalculator(List<EItemOrdine> itemOrderList, int ordiniInPreparazione, String indirizzoCliente) {
        int tempo = 0;

        for (EItemOrdine item : itemOrderList) {
            String categoria = item.getProdotto().getCategoria().getNome();
            int quantita = item.getQuantita();
            int tempoBase = tempiPerCategoria.getOrDefault(categoria, 0);
            tempo += tempoBase * quantita;
        }

        if (ordiniInPreparazione >= 8) {
            tempo += 10;
        }

        if (indirizzoCliente != null && !indirizzoCliente.isEmpty()) {
            tempo += tempoTrasportoCalculator(indirizzoCliente);
        } else {
            tempo += 30;
        }

        return tempo;
    }

    private int tempoTrasportoCalculator(String indirizzoDestinazione) {
        try {
            String origine = java.net.URLEncoder.encode(indirizzoRistorante, "UTF-8");
            String destinazione = java.net.URLEncoder.encode(indirizzoDestinazione, "UTF-8");
            String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                    origine + "&destinations=" + destinazione + "&key=" + apiKey;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder jsonString = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonString.append(inputLine);
                }
            }

            JSONObject response = new JSONObject(jsonString.toString());
            int durationInSeconds = response
                    .getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0)
                    .getJSONObject("duration")
                    .getInt("value");

            return (int) Math.ceil(durationInSeconds / 60.0);

        } catch (Exception e) {
            e.printStackTrace();
            return 30; // fallback
        }
    }

    public LocalDateTime orarioConsegnaCalculator(List<EItemOrdine> itemOrderList, int ordiniInPreparazione, String indirizzoCliente) {
        int minuti = timeCalculator(itemOrderList, ordiniInPreparazione, indirizzoCliente);
        return LocalDateTime.now().plusMinutes(minuti);
    }
}
