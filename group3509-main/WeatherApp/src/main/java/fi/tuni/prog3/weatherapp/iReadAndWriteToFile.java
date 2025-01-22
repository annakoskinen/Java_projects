package fi.tuni.prog3.weatherapp;

/**
 * Rajapinta metodeille lukea ja kirjoittaa dataa tiedostojen kautta.
 */
public interface iReadAndWriteToFile {

    /**
      Lukee JSON dataa tiedostosta.
     * @param fileName luettavan tiedoston nimi.
     * @return recentCity viimeisimpänä avattu sijainti tallennettuna.
     * @throws Exception jos metodi ei suostu avaamaan tiedostoa/jäsentämään.
     */
    public String readFromFile(String fileName) throws Exception;

    /**
     * Kirjoittaa JSON dataa tiedostoon.
     * @param fileName luettavan tiedoston nimi.
     * @param favorites suosikkisijainnit tallennettuna,
     * @param recentCity viimeisimpänä avattu sijainti tallennettuna.
     * @throws Exception jos metodi ei suostu kirjoittamaan tiedostoon.
     */
    public void writeToFile(String fileName, Favorites favorites,
            String recentCity) throws Exception;
}
