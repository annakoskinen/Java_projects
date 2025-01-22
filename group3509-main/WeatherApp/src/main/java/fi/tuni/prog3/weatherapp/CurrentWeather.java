package fi.tuni.prog3.weatherapp;

/**
 * Luokka nykyistä säädataa varten.
 */

public class CurrentWeather {
    /**
     * Sääid sääikoneita varten.
     */
    private long id = 0;
    
    /**
     * Lämpötila.
     */
    private double temp = 0;
    
    /**
     * Lämpötilan tuntuma.
     */
    private double feelsLike = 0;
    
    /**
     * Tuulen nopeus.
     */
    private double wind = 0;
    
    /**
     * Sädemäärä.
     */
    private double rain = 0;

    /**
     * Rakennin, joka ottaa parametreinään nykyisen sään tietoja.
     * @param id sääid
     * @param temp lämpötila
     * @param feelsLike lämpötilan tuntuma
     * @param wind tuulinopeus
     * @param rain sademäärä
     */
    public CurrentWeather(long id, double temp, double feelsLike, double wind, 
            double rain) {
        this. id = id;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.wind = wind;
        this.rain = rain;  
    }
    
    /**
     * Palauttaa sääid:n.
     * @return sääid
     */
    public long getId() {
        return id;
    }
    
    /**
     * Palauttaa lämpötilan arvon.
     * @return lämpötila
     */
    public double getTemp() {
        return temp;
    }
    
    /**
     * Palauttaa lämpötilan tuntuman arvon.
     * @return lämpötilan tuntuma
     */
    public double getFeelsLike() {
        return feelsLike;
    }
    
    /**
     * Palauttaa tuulen nopeuden.
     * @return tuulen nopeus
     */
    public double getWind() {
        return wind;
    }
    
    /**
     * Palauttaa sademäärän.
     * @return sademäärä
     */
    public double getRain() {
        return rain;
    }   
}
