package fi.tuni.prog3.weatherapp;

/**
 * Luokka tuntikohtaisen sääennusteen säilyttämista varten.
 */
public class HourlyForecast {
    /**
     * Päivämäärä, joka sisältää päivämäärän ja kellonajan.
     */
    private String date;
    
    /**
     * Lämpötila.
     */
    private double temp;
    
    /**
     * Lämpötilan tuntuma.
     */
    private double feelsLike;
    
    /**
     * Sateen todennäköisyys.
     */
    private double pop;
    
    /**
     * Sääid sääikoneita varten.
     */
    private long id;
    
    /**
     * Rakennin, joka ottaa parametreinään tuntikohtaisen sääennusteen tietoja.
     * @param date päivämäärä
     * @param temp lämpötila
     * @param feelsLike lämpötilan tuntuma
     * @param pop sateen todennäköisyys
     * @param id sääid
     */
    public HourlyForecast(String date, double temp, double feelsLike,
            double pop, long id) {
        this.date = date;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pop = pop;
        this.id = id;
    }
    
    /**
     * Palauttaa päivämäärän ja kellonajan.
     * @return päivämäärä
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Palauttaa lämpötilan.
     * @return lämpötila
     */
    public double getTemp() {
        return temp;
    }
    
    /**
     * Palauttaa lämpötilan tuntuman.
     * @return lämpötilan tuntuma
     */
    public double getFeelsLike() {
        return feelsLike;
    }
    
    /**
     * Muuttaa sateen todennäköisyyden prosenteiksi ja palauttaa arvon.
     * @return sateen todennäköisyys prosentteina
     */
    public String getPop() {
        String fpop = String.format("%.0f", pop*100);
        return fpop;
    }
    
    /**
     * Palauttaa sääid:n.
     * @return sääid
     */
    public long getId() {
        return id;
    }
}
