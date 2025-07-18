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

    //Mappa che assegna ad ogni categoria dei tempi predefiniti
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

    
    //Metodo che calcola il tempo di preparazione necessario per preparare un ordine
    //considerando la categoria di appartenenza, la quantità e gli ordini che sono in preparazione in cucina
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

    //Metodo che calcola il tempo di trasporto necessario a portare l'ordine verso l'indirizzo di consegna selezionato
    //Attraverso un'api maps 
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

    //Metodo che verifica se l'orario calcolato fino ad ora può essere compatibile 
    //con il calendario del ristorante chiamando il metodo trovaPrimoOrarioAperto()
    public LocalDateTime orarioConsegnaCalculator(List<EItemOrdine> itemOrderList, int ordiniInPreparazione, String indirizzoCliente) throws Exception {
        int minuti = timeCalculator(itemOrderList, ordiniInPreparazione, indirizzoCliente);
        LocalDateTime orarioPrevisto = LocalDateTime.now().plusMinutes(minuti);

        return trovaPrimoOrarioAperto(orarioPrevisto, minuti);
    }
    
    //Metodo che cerca il primo giorno aperto del ristorante da proporre come data di consegna 
    private LocalDateTime trovaPrimoOrarioAperto(LocalDateTime orario, int timePT) throws Exception {
        while (true) {
            
            
            //Verifica se per quel giorno c'è una chiusura straordinaria prevista
            EExceptionCalendario eccezione = getEccezionePerData(orario.toLocalDate());
            //Recupera il giorno settimanale e l'orario di chiusura per quel giorno nel calendario
            String giornoSettimanaIT = getNomeGiornoItaliano(orario);
            ECalendario orarioSettimana = getOrarioSettimanalePerGiorno(giornoSettimanaIT);

            //Se in quel giorno il ristorante è chiuso, passa alla mezzanotte del giorno successivo 
            if (orarioSettimana == null || !orarioSettimana.isAperto() || (eccezione != null && !eccezione.isAperto())) {
                orario = orario.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                continue;
            }

            LocalTime apertura = orarioSettimana.getOrarioApertura();
            LocalTime chiusura = orarioSettimana.getOrarioChiusura();

            //Se l'orario è precedente all'apertura si aggiunge semplicemente timePT a partire dall'apertura
            //Ovvero il tempo PREPARAZIONE e TRASPORTO
            if (orario.toLocalTime().isBefore(apertura)) {
                orario = orario.withHour(apertura.getHour()).withMinute(apertura.getMinute());
                orario = orario.plusMinutes(timePT);
                break;
            } else if (orario.toLocalTime().isAfter(chiusura)) { //se l'orario è successivo alla chiusura si passa al giorno successivo 
                orario = orario.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                continue;
            } else {
                // dentro orario apertura
                break;
            }
        }
        return orario; //Qui restituiamo l'orario che verrà proposto al cliente
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

    //Verifica se una certa data fa parte dei giorni di chiusura eccezzionale
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

    //Se il giorno fa parte dei weeklyCloseDays restituiamo null
    for (ECalendario closedDay : weeklyClosedDays) {
        if (closedDay.getData() == dayOfWeek) {
            return null;
        }
    }

    //Se il giorno fa parte dei weeklyOpenDays restituiamo il giorno, che al suo interno contiene orario
    //di apertura e orario di chiusura, necessari per fare i controlli della data di consegna
    for (ECalendario openDay : weeklyOpenDays) {
        if (openDay.getData() == dayOfWeek) {
            return openDay;
        }
    }
    return null;
}

}