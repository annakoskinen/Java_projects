package fi.tuni.prog3.weatherapp;

import java.util.ArrayList;
import org.json.simple.JSONArray;

/**
 * Luokka suosikkikaupunkien tallentamista varten.
 */
public class Favorites {
    
    private ArrayList<String> favorites = new ArrayList<>();
    
    public Favorites() {
    }
    
    /**
     * Lisätään suosikki.
     * @param fav 
     */
    public void setFavorite(String fav) {
        this.favorites.add(fav);        
    }
    
    /**
     * Poistetaan suosikki.
     * @param fav
     */
    public void removeFavorite(String fav) {
        this.favorites.remove(fav);
    }
    
    /**
     * Haetaan suosikit.
     * @return favorites.
     */
    public ArrayList<String> getFavorites() {
        return favorites;
    }
    
    /**
     * Muokataan suosikit JSON taulukkoon.
     **/
    public JSONArray toJSON() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(favorites);
        return jsonArray;
    }

    /**
     * Alustetaan suosikit pois JSON taulukosta.
     * @param jsonArray 
     */
    public void fromJSON(JSONArray jsonArray) {
        this.favorites.clear();
        if (jsonArray != null) {
        for (Object obj : jsonArray) {
            if (obj instanceof String) {
                this.favorites.add((String) obj);
            }
        }
        }
    }
}
