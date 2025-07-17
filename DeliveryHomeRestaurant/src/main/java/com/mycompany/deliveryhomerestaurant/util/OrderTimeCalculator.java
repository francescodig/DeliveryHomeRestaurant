package com.mycompany.deliveryhomerestaurant.util;

import com.mycompany.deliveryhomerestaurant.DAO.ECalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.EExceptionCalendarioDAO;
import com.mycompany.deliveryhomerestaurant.DAO.impl.ECalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.DAO.impl.EExceptionCalendarioDAOImpl;
import com.mycompany.deliveryhomerestaurant.Model.*;
import jakarta.persistence.EntityManager;

import java.time.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderTimeCalculator {

    private final Map<String, Integer> tempiPerCategoria = Map.of(
            "Antipasti", 5,
            "Primi", 10,
            "Secondi", 10,
            "Dolci", 5,
            "Bevande", 0
    );

    private final String indirizzoRistorante;
    private final String apiKey;
    private final EntityManager em;

    public OrderTimeCalculator(EntityManager em) {
        this.apiKey = Constants.API_KEY;
        this.indirizzoRistorante = Constants.INDIRIZZO_RISTORANTE;
        this.em = em;
    }

    public int timeCalculator(List<EItemOrdine> itemOrderList, int ordiniInPreparazione, String indirizzoCliente) throws Exception {
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

    private int tempoTrasportoCalculator(String indirizzoDestinazione) throws Exception {
        String origine = java.net.URLEncoder.encode(indirizzoRistorante, "UTF-8");
        String destinazione = java.net.URLEncoder.encode(indirizzoDestinazione, "UTF-8");
        String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origine
                + "&destinations=" + destinazione + "&key=" + apiKey;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            return 30; // fallback
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder responseStrBuilder = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            responseStrBuilder.append(inputLine);
        }
        in.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseStrBuilder.toString());

        try {
            int seconds = root.path("rows").get(0).path("elements").get(0).path("duration").path("value").asInt();
            return (int) Math.ceil(seconds / 60.0);
        } catch (Exception e) {
            return 30; // fallback
        }
    }

    public LocalDateTime orarioConsegnaCalculator(List<EItemOrdine> itemOrderList, int ordiniInPreparazione, String indirizzoCliente) throws Exception {
        int minuti = timeCalculator(itemOrderList, ordiniInPreparazione, indirizzoCliente);
        LocalDateTime orarioPrevisto = LocalDateTime.now().plusMinutes(minuti);

        return trovaPrimoOrarioAperto(orarioPrevisto, minuti);
    }

    private LocalDateTime trovaPrimoOrarioAperto(LocalDateTime orario, int timePT) throws Exception {
        while (true) {
            String giornoSettimanaIT = getNomeGiornoItaliano(orario);
            EExceptionCalendario eccezione = getEccezionePerData(orario.toLocalDate());
            ECalendario orarioSettimana = getOrarioSettimanalePerGiorno(giornoSettimanaIT);

            if (orarioSettimana == null || !orarioSettimana.isAperto() || (eccezione != null && !eccezione.isAperto())) {
                orario = orario.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                continue;
            }

            LocalTime apertura = orarioSettimana.getOrarioApertura();
            LocalTime chiusura = orarioSettimana.getOrarioChiusura();

            if (orario.toLocalTime().isBefore(apertura)) {
                orario = orario.withHour(apertura.getHour()).withMinute(apertura.getMinute());
                orario = orario.plusMinutes(timePT);
                break;
            } else if (orario.toLocalTime().isAfter(chiusura)) {
                orario = orario.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                continue;
            } else {
                // dentro orario apertura
                break;
            }
        }
        return orario;
    }

    private String getNomeGiornoItaliano(LocalDateTime dateTime) {
        return switch (dateTime.getDayOfWeek()) {
            case MONDAY -> "lunedì";
            case TUESDAY -> "martedì";
            case WEDNESDAY -> "mercoledì";
            case THURSDAY -> "giovedì";
            case FRIDAY -> "venerdì";
            case SATURDAY -> "sabato";
            case SUNDAY -> "domenica";
        };
    }

    private EExceptionCalendario getEccezionePerData(LocalDate date) throws Exception {
        EExceptionCalendarioDAO calendarioDAO = new EExceptionCalendarioDAOImpl(em);
        List<EExceptionCalendario> exceptionClosedDays =  calendarioDAO.getGiorniChiusureStraordinarie();
        for (EExceptionCalendario e : exceptionClosedDays) {
            if (e.getExceptionDate().isEqual(date)) {
                return e;
            }
        }
        return null;
    }
    
    private String giornoItalianoToEnglishUpper(String giornoIT) {
    return switch (giornoIT.toLowerCase()) {
        case "lunedì" -> "MONDAY";
        case "martedì" -> "TUESDAY";
        case "mercoledì" -> "WEDNESDAY";
        case "giovedì" -> "THURSDAY";
        case "venerdì" -> "FRIDAY";
        case "sabato" -> "SATURDAY";
        case "domenica" -> "SUNDAY";
        default -> "";
    };
}


    private ECalendario getOrarioSettimanalePerGiorno(String giornoIT) throws Exception {
    ECalendarioDAO calendarioDAO = new ECalendarioDAOImpl(em);
    List<ECalendario> weeklyClosedDays = calendarioDAO.getGiorniChiusureSettimanali();
    List<ECalendario> weeklyOpenDays = calendarioDAO.getGiorniApertureSettimanali();

    String giornoEnglishUpper = giornoItalianoToEnglishUpper(giornoIT);
    DayOfWeek dayOfWeek = DayOfWeek.valueOf(giornoEnglishUpper);

    for (ECalendario closedDay : weeklyClosedDays) {
        if (closedDay.getData() == dayOfWeek) {
            return null;
        }
    }

    for (ECalendario openDay : weeklyOpenDays) {
        if (openDay.getData() == dayOfWeek) {
            return openDay;
        }
    }
    return null;
}

}