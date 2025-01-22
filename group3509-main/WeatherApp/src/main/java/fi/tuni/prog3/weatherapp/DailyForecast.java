package fi.tuni.prog3.weatherapp;

/**
 * Luokka päiväkohtaisen sääennusteen säilyttämista varten.
 */
public class DailyForecast {
    
    /**
     * Päivämäärä.
     */
    private String date;
    
    /**
     * Päivälämpötila.
     */
    private double temp_day;
    
    /**
     * Yölämpötila.
     */
    private double temp_night;
    
    /**
     * Sateen todennäköisyys.
     */
    private double pop;
    
    /**
     * Sääid sääikoneita varten.
     */
    private long id;
    
    /**
     * Rakennin, joka ottaa parametreinään päivittäisen sääennusteen tietoja.
     * @param date päivämäärä
     * @param temp_day päivälämpötila
     * @param temp_night yölämpötila
     * @param pop sateen todennäköisyys
     * @param id sääid
     */
    public DailyForecast(String date, double temp_day, double temp_night,
            double pop, long id) {
        this.date = date;
        this.temp_day = temp_day;
        this.temp_night = temp_night;
        this.pop = pop;
        this.id = id;
    }
    
    /**
     * Palauttaa päivämäärän.
     * @return päivämäärä
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Palauttaa keskipäivän lämpötilan.
     * @return lämpötila
     */
    public double getTemp_day() {
        return temp_day;
    }
    
    /**
     * Palauttaa keskiyön lämpötilan.
     * @return lämpötila
     */
    public double getTemp_night() {
        return temp_night;
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
