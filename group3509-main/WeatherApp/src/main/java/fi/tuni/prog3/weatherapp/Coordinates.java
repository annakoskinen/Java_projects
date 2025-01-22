package fi.tuni.prog3.weatherapp;

/**
 * Luokka kaupungin koordinaatteja varten.
 */
public class Coordinates {
    
    /**
     * Kaupungin leveysaste.
     */
    private double lat = 0;
    
    /**
     * Kaupungin pituusaste.
     */
    private double lon = 0;
    
    /**
     * Rakennin, joka ottaa parametreinään kaupungin koordinaatit.
     * @param lat leveysaste
     * @param lon pituusaste
     */
    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
    /**
     * Palauttaa leveysasteen.
     * @return leveysaste
     */
    public double getLat() {
        return lat;
    }
    
    /**
     * Palauttaa pituusasteen
     * @return pituusaste
     */
    public double getLon() {
        return lon;
    }
}
