package fi.tuni.prog3.weatherapp;

import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit testit luokalle API.
 */
public class APITest {
    
    public APITest() {
    }
    
    /**
     * Testi lookUpLocation metodille, luokassa API.
     */
    @Test
    public void testLookUpLocation() throws IOException {
        // Testi oikeellisella kaupungin nimellä.
        Coordinates coordinates = API.lookUpLocation("London");
        
        // Testataan oikeellisilla koordinaateilla.
        assertNotNull(coordinates);
        assertEquals(51.5074, coordinates.getLat(), 0.001);
        assertEquals(-0.1278, coordinates.getLon(), 0.001);

        // Testi ei sopivalla kaupungin nimellä.
        Coordinates nullCoordinates = API.lookUpLocation("InvalidCityName");
        assertNull(nullCoordinates);
    }

    /**
     * Testi getCurrentWeather metodille, luokassa API.
     */
    @Test
    public void testGetCurrentWeather() throws IOException {
        // Testi oikeellisilla leveys- ja pituusasteilla.
        CurrentWeather weatherData = API.getCurrentWeather(51.5074, -0.1278);
        
        // Testit eri metodin parametreille.
        assertNotNull(weatherData);
        assertTrue(weatherData.getId() >= 0);
        
        // Lämpötilojen tulisi olla järkeviä.
        assertTrue(weatherData.getTemp() > -100 && weatherData.getTemp() < 100);
        assertTrue(weatherData.getFeelsLike() > -100 
                && weatherData.getFeelsLike() < 100);
        
        // Tuulen nopeuden tulisi olla epänegatiivinen.
        assertTrue(weatherData.getWind() >= 0);
        
        // Sateen määrän tulisi epänegatiivinen
        assertTrue(weatherData.getRain() >= 0);

        // Testi epäsopivilla koordinaateilla.
        CurrentWeather nullWeatherData = API.getCurrentWeather(1000, 1000);
        assertNull(nullWeatherData);
    }



    /**
     * Testi getDailyForecast metodille, luokassa API.
     */
    @Test
    public void testGetDailyForecast() throws IOException {
        // Testi oikeellisella kaupungin nimellä.
        Map<Integer, DailyForecast> dailyForecastMap = API.getDailyForecast("Tampere");
        
        // Testataan palauttaako metodi null ja onko map epätyhjä.
        assertNotNull(dailyForecastMap);
        assertFalse(dailyForecastMap.isEmpty());
        
        // Ennusteiden määrän tulee olla maksimissaan 7.
        assertTrue(dailyForecastMap.size() <= 7);
        
        // Testi epäsopivalla kaupungin nimellä.
        Map<Integer, DailyForecast> nullForecastMap = API.getDailyForecast("InvalidCityName");
        
        // Testataan palauttaako metodi null tarvittaessa.
        assertNull(nullForecastMap);
    }

    /**
     * Testi getHourlyForecast metodille, luokassa API.
     */
    @Test
    public void testGetHourlyForecast() throws IOException {
        // Testi oikeellisilla koordinaateilla.
        Map<Integer, HourlyForecast> hourlyForecastMap = API.getHourlyForecast(51.5074, -0.1278);
        
        // Testataan palauttaako metodi null ja onko map epätyhjä.
        assertNotNull(hourlyForecastMap);
        assertFalse(hourlyForecastMap.isEmpty());
        
        // Testi epäsopivilla koordinaateilla.
        Map<Integer, HourlyForecast> nullHourlyForecastMap = API.getHourlyForecast(1000, 1000);
        
        // Testataan palauttaako metodi null tarvittaessa.
        assertNull(nullHourlyForecastMap);
    }
}
