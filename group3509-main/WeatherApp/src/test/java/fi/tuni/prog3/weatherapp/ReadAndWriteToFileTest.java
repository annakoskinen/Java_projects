package fi.tuni.prog3.weatherapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;

/**
 * Testit luokalle ReadAndWriteToFile.
 */
public class ReadAndWriteToFileTest {

    private Favorites favorites;
    private String recentCity;
    private ReadAndWriteToFile fileHandler;

    @BeforeEach
    public void setUp() {
        favorites = new Favorites();
        recentCity = "Helsinki";
        fileHandler = new ReadAndWriteToFile(favorites, recentCity);
    }

    @Test
    public void testReadFromFile_ValidFile() {
        try {
            // Luodaan väliaikainen tiedosto JSON datalla.
            File tempFile = File.createTempFile("temp", ".json");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write("{\"favorites\": [], \"recentCity\": \"Tampere\"}");
            }

            String city = fileHandler.readFromFile(tempFile.getAbsolutePath());
            assertEquals("Tampere", city);
        } 
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Testi writeToFile metodin datan oikeellisuudelle.
     */
    @Test
    public void testWriteToFile_ValidData() {
        try {
            // Luodaan väliaikainen tiedosto kirjoittamista varten.
            File tempFile = File.createTempFile("temp", ".json");

            fileHandler.writeToFile(tempFile.getAbsolutePath(), favorites, recentCity);

            // Luetaan dataa tiedostosta ja varmennetaan se.
            String city = fileHandler.readFromFile(tempFile.getAbsolutePath());
            assertEquals(recentCity, city);
        } 
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Testi metodille writeToFile tiedoston nimen tarkistusta varten.
     */
    @Test
    public void testWriteToFile_NullFileName() {
        assertThrows(NullPointerException.class, () -> {
            fileHandler.writeToFile(null, favorites, recentCity);
        });
    }
}
