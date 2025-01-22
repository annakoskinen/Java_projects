package fi.tuni.prog3.weatherapp;

import java.io.File;
import java.util.HashMap;
import javafx.scene.image.Image;

/**
 * Luokka sääikoneita varten.
 */
public class Icons {
    
    /**
     * HashMap, joka sisältää sääid:n ja sitä vastaavan sääikonin.
     */
    private HashMap<String, Image> icons = new HashMap<>();
    
    /**
     * Rakennin, joka täydentää HashMapin ikoneilla.
     */
    public Icons() {
        // Myrsky
        File storm = new File("icons/storm.png");
        this.icons.put("2", new Image(storm.toURI().toString()));
        
        // Tihkusade
        File drizzle = new File("icons/drizzle.png");
        this.icons.put("3", new Image(drizzle.toURI().toString()));
        
        // Sade
        File rainy = new File("icons/rain.png");
        this.icons.put("5", new Image(rainy.toURI().toString()));
        
        // Lumisade
        File snow = new File("icons/snow.png");
        this.icons.put("6", new Image(snow.toURI().toString()));
        
        //
        File atmosphere = new File("icons/atmosphere.png");
        this.icons.put("7", new Image(atmosphere.toURI().toString()));
        
        // Selkeä
        File clear = new File("icons/clear.png");
        this.icons.put("800", new Image(clear.toURI().toString()));
        
        // Pilvinen
        File clouds = new File("icons/clouds.png");
        this.icons.put("80", new Image(clouds.toURI().toString()));
    }
    
    /**
     * Ottaa parametrinään sääid:n ja palauttaa sitä vastaavan sääikonin.
     * @param id kyseisen päivän tai ajankohdan sääid
     * @return sääikoni
     */
    public Image getWeatherIcon(long id) {
        if(id >= 200 && id < 300) {
            return icons.get("2");
        }
        else if(id >= 300 && id < 400) {
            return icons.get("3");
        }
        else if(id >= 500 && id < 600) {
            return icons.get("5");
        }
        else if(id >= 600 && id < 700) {
            return icons.get("6");
        }
        else if(id >= 700 && id < 800) {
            return icons.get("7");
        }
        else if(id == 800) {
            return icons.get("800");
        }
        else {
            return icons.get("80");
        }
    }  
}
