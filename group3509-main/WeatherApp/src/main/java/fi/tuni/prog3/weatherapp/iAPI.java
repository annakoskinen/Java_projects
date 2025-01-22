package fi.tuni.prog3.weatherapp;

import java.util.Map;

/**
 * Rajapinta OpenWeatherMap APIn datan poimimiseen.
 */
public interface iAPI {

    /**
     * Palauttaa koordinaatit sijainnille.
     * @param city Sijainnin nimi haettaville koordinaateille.
     * @return Coordinates objekti koordinaateille.
     */
    public Coordinates lookUpLocation(String city);

    /**
     * Palauttaa tämänhetkisen sään saatujen koordinaattien sijainnille.
     * @param lat Sijainnnin leveysaste.
     * @param lon Sijainnin pituusaste.
     * @return CurrentWeather objekti tämänhetkiselle säälle.
     */
    public CurrentWeather getCurrentWeather(double lat, double lon);

    /**
     * Palauttaa päiväkohtaisen sään saatujen koordinaattien sijainnille.
     * @param city haettava sijainti.
     * @return Map päiväkohtaisen sään tallennukseen indeksoidussa muodossa.
     */
    public Map<Integer, DailyForecast> getDailyForecast(String city);
    
    /**
     * Palauttaa tuntikohtaisen sään saatujen koordinaattien sijainnille.
     * @param lat Sijainnnin leveysaste.
     * @param lon Sijainnin pituusaste.
     * @return Map tuntikohtaisen sään tallennukseen indeksoidussa muodossa.
     */
    public Map<Integer, HourlyForecast> getHourlyForecast(double lat, double lon);
}
