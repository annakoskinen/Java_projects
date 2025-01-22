package fi.tuni.prog3.weatherapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Scanner;
import org.json.simple.parser.ParseException;


public class API {
    public static Coordinates lookUpLocation(String city) {
        try {
            // Kaupungin API kutsun URL koordinaateille
            String urlStringCity = "http://api.openweathermap.org/geo/1.0/direct?q="+
                city + "&limit=1&appid=97560a77643d34eef1accb52db691258";
            
            // Etsitään sijainnin dataa
            String jsonResponse = searchConnection(urlStringCity);
            
            // Jäsennetään tuloksen merkkijono JSON objektiin
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(jsonResponse);

            JSONObject locationObj = null;
            
            // Haetaan sijainnin dataa ja koordinaatit
            for (Object obj : jsonArray) {
                locationObj = (JSONObject) obj;
            }
            if (locationObj != null) {
                double lat = (double) locationObj.get("lat");
                double lon = (double) locationObj.get("lon");
                
                // Luodaan objekti koordinaateille
                Coordinates coordinates = new Coordinates(lat, lon);

                return coordinates;
            }
            else {
                return null;
            }
        } 
        catch(ParseException e){
            return null;
        }
    }
    
    public static CurrentWeather getCurrentWeather(double lat, double lon) {
        // Kaupungin API kutsun URL tämänhetkiselle säälle
        String urlStringWeather = "https://api.openweathermap.org/data/2.5/weather?lat="
                + lat + "&lon=" + lon + "&units=metric&appid=97560a77643d34eef1accb52db691258";

        try {
             // Haetaan tämänhetkistä säädataa
            String jsonResponse = searchConnection(urlStringWeather);
            if(jsonResponse == null) {
                return null;
            }
            else {
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(jsonResponse);

                // Haetaan sään tunniste (id)
                JSONArray weatherArray = (JSONArray) resultJsonObj.get("weather");
                JSONObject weatherObj = (JSONObject) weatherArray.get(0);
                long weatherID = (long) weatherObj.get("id");

                 // Haetaan lämpötila
                JSONObject mainObj = (JSONObject) resultJsonObj.get("main");
                double temp = (double) mainObj.get("temp");
                double feelsLike = (double) mainObj.get("feels_like");

                // Haetaan tuulen nopeus
                JSONObject windObj = (JSONObject) resultJsonObj.get("wind");
                double wind = (double) windObj.get("speed");

                // Haetaan sademäärä
                JSONObject rainObj = (JSONObject) resultJsonObj.get("rain");
                double rain;
                if(rainObj != null) {
                    rain = (double) rainObj.get("1h");
                }
                // Jos sademäärää ei todettu niin asetetaan arvoksi 0.
                else {
                    rain = 0.0;
                }
                // Luodaan ja palautetaan uusi tämänhetkisen sään datan objekti
                CurrentWeather weather = new CurrentWeather(weatherID, temp, 
                        feelsLike, wind, rain);

                return weather;
            }
        } 
        catch(ParseException e){
            return null;
        }
    }
    
    public static Map<Integer, DailyForecast> getDailyForecast(String city) {
        // Kaupungin API kutsun URL päiväkohtaisen koonnin ennusteelle
        String urlStringForecast = "http://api.openweathermap.org/data/2.5/forecast"
                + "/daily?q=" + city + "&cnt=7&appid=a884f5d6870aaf0adf5447d73d059c9c"
                + "&units=metric";
        
        try {
            // Haetaan päiväkohtaista säädataa
            String jsonResponse = searchConnection(urlStringForecast);
            if(jsonResponse == null) {
                return null;
            }
            else {
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(jsonResponse);

                // Luodaan HashMap säädatan tallennusta varten
                Map<Integer, DailyForecast> ForecastDataMap = new HashMap<>();
                JSONArray forecastList = (JSONArray) resultJsonObj.get("list");
                
                int index = 0;
                for (Object obj : forecastList) {
                    JSONObject dayObj = (JSONObject) obj;

                    // Haetaan päivämäärä UNIX muodosta standardiin muotoon
                    long timestamp = (long) dayObj.get("dt");
                    String date = Instant.ofEpochSecond(timestamp)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                            .format(DateTimeFormatter.ofPattern("EE dd.MM."));

                    // Haetaan lämpötila erikseen päivälle ja yölle
                    JSONObject tempObj = (JSONObject) dayObj.get("temp");
                    double temp_day = getDoubleFromObject(tempObj.get("day"));
                    double temp_night = getDoubleFromObject(tempObj.get("night"));

                    // Haetaan säädatan tunniste (id)
                    JSONArray weatherArray = (JSONArray) dayObj.get("weather");
                    JSONObject weatherObj = (JSONObject) weatherArray.get(0);
                    long weatherID = (long) weatherObj.get("id");

                    // Haetaan sateen todennäköisyys
                    double pop = getDoubleFromObject(dayObj.get("pop"));

                    // Luodaan ja palautetaan päiväkohtaiselle ennusteelle objekti
                    DailyForecast dailyforecast = new DailyForecast(date, 
                            temp_day, temp_night, pop, weatherID);

                    ForecastDataMap.put(index++, dailyforecast);
                }
                return ForecastDataMap;
            }            
        } 
        catch(ParseException e){    
            return null;
        }
    }
    
    public static Map<Integer, HourlyForecast> getHourlyForecast(double lat, double lon) {
        // Kaupungin API kutsun URL tuntikohtaiselle säälle
        String urlStringForecast = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat="
                 + lat + "&lon=" + lon + "&units=metric&cnt=26&appid=a884f5d6870aaf0adf5447d73d059c9c";
        
        try {
            // Etsitään tuntikohtaista säädataa
            String jsonResponse = searchConnection(urlStringForecast);
            if(jsonResponse == null) {
                return null;
            }
            else {
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(jsonResponse);

                JSONArray forecastList = (JSONArray) resultJsonObj.get("list");

                Map<Integer, HourlyForecast> hourlyWeatherMap = new HashMap<>();

                int index = 0;
                for (Object obj : forecastList) {
                    JSONObject jsonObj = (JSONObject) obj;

                    // Haetaan päivämäärä
                    String date = (String) jsonObj.get("dt_txt");
                    JSONObject mainObj = (JSONObject) jsonObj.get("main");
                        
                    // Haetaan lämpötila
                    double temp = getDoubleFromObject(mainObj.get("temp"));
                    double feelsLike = getDoubleFromObject(mainObj.get("feels_like"));

                    // Haetaan säädatalle tunniste (id)
                    JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
                    JSONObject weatherObj = (JSONObject) weatherArray.get(0);   
                    
                    long weatherID = (long) weatherObj.get("id");

                    // Haetaan sateen todennäköisyys
                    double pop = getDoubleFromObject(jsonObj.get("pop"));

                    HourlyForecast hourlyforecast = new HourlyForecast(date,
                            temp, feelsLike, pop, weatherID);                    

                    hourlyWeatherMap.put(index++, hourlyforecast);
                    }
                return hourlyWeatherMap;
            }
            } 
        catch(ParseException e){
            return null;
        }
    }
    
    /**
     * Palauttaa API kutsulta saaman vastauksen JSON muodossa
     * @param urlString API kutsu merkkijonona
     * @return jsonResponse/null
     */
    private static String searchConnection(String urlString) {
        // Haetaan yhteyttä APIin
        try {
             HttpURLConnection conn = fetchApiResponse(urlString);

            if(conn.getResponseCode() != 200){
//                System.out.println("Error: Could not connect to API");
                return null;
            }

             // Luetaan vastaus json datasta merkkijonoksi
            String jsonResponse = readApiResponse(conn);
            return jsonResponse;
        }
        catch(IOException e){
            return null;
        }
    }
    
    /**
     * Suorittaa APIn vastauksen haun
     * @param urlString
     * @return conn yhteyden APIin
     */
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try{
            // Yritetään luoda yhteys
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Valitaan metodiksi GET
            conn.setRequestMethod("GET");

            return conn;
        }
        catch(IOException e){
            return null;
        }
    }
    
    /**
     * Lukee APIn vastauksen ja muuttaa sen tarkasteltavaan muotoon merkkijonoksi
     * @param apiConnection yhteys APIin
     * @return resultJson merkkijonona
     */
    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            // Luodaan StringBuilder Json datan tallentamiseksi
            StringBuilder resultJson = new StringBuilder();

            // Luodaan skanneri, jolla luetaan yhteyden tulos
            Scanner scanner = new Scanner(apiConnection.getInputStream());

            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }
            
            scanner.close();

            // Palautetaan Json data merkkijonona
            return resultJson.toString();

        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Tekoälyavusteisesti luotu objektin double muotoon saava funktio jos
     * tulos on tasan 0
     * @param obj doubleksi muokattava objekti
     * @return doubleksi muokattu objekti
     */
    private static double getDoubleFromObject(Object obj) {
        // Jos objekti on jo tyyppiä numero
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();          
        } 
        // Jos objekti on tyyppiä String
        else if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        } 
        
        // Varaudutaan poikkeuksiin
        else {
            throw new IllegalArgumentException("Invalid object type for "
                    + "conversion to double: " + obj.getClass());
        }
    }
}
