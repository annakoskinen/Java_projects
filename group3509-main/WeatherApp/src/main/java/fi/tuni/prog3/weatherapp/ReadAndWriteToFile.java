package fi.tuni.prog3.weatherapp;

import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadAndWriteToFile {
    private Favorites favorites;
    private String recentCity;
    
    public ReadAndWriteToFile(Favorites favorites, String recentCity) {
        this.favorites = favorites;
        this.recentCity = recentCity;
    }
    
    public String readFromFile(String fileName) throws Exception {
        // Yritetään lukea tallennettu Json muotoinen tieto tiedostosta.
        try (FileReader reader = new FileReader(fileName)) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(reader);

            // Haetaan suosikit ja viimeiseksi tarkasteltu sijainti.
            JSONArray favoritesArray = (JSONArray) data.get("favorites");
            recentCity = (String) data.get("recentCity");

            // Epäsarjallistetaan suosikit taulukosta
            favorites.fromJSON(favoritesArray);
            
        } catch (IOException | ParseException e) {
            throw new IOException("Virhe tiedoston lukemisessa tai "
                    + "JSON-muodon parsimisessa", e);
        }
        return recentCity;
    }
    
    public void writeToFile(String fileName, Favorites favorites, 
            String recentCity) throws Exception {
        // Alustetaan tiedostoon kirjoitus.
        try (FileWriter file = new FileWriter(fileName)) {
            JSONObject data = new JSONObject();
            
            // Sijoitetaan otsikoille vastaavat tiedot.
            data.put("favorites", favorites.toJSON());
            data.put("recentCity", recentCity);

            // Yritetään kirjoittaa tiedostoon Json muodossa.
            file.write(data.toJSONString());
            
        }   
        catch (IOException e) {
            throw new IOException("Virhe tiedostoon kirjoittamisessa", e);
        }
    }
}

