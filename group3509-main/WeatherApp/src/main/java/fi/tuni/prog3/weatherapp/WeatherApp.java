package fi.tuni.prog3.weatherapp;

import java.io.File;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;


/**
 * JavaFX Sääapplikaatio nimeltään MitäsSää?.
 */
public class WeatherApp extends Application {
    
    /**
     * Sovelluksen näyttämö
     */
    private Stage stage;
    /**
     * Pääikkunan kehys
     */
    private BorderPane root;
    /**
     * Suosikit-ikkunan kehys
     */
    private BorderPane root2;
    /**
     * Pääikkunan skene
     */
    private Scene scene;
    /**
     * Suosikit-ikkunan skene
     */
    private Scene scene2;
    
    /**
     * Hakukenttään syötetty hakusana
     */
    private String searchText = ""; 
    /**
     * Nykyisen sään luokalle muuttuja
     */
    private CurrentWeather cw;
    /**
     * Sovelluksien sääikonien luokalle muuttuja
     */
    private static Icons icons = new Icons();
    /**
     * Alustetaan tietorakenne päiväkohtaisen datan hakua varten
     */
    private Map<Integer, DailyForecast> forecastDataMap;
    /**
     * Alustetaan tietorakenne tuntikohtaisen datan hakua varten
     */
    private Map<Integer, HourlyForecast> hourlyWeatherMap;
    /**
     * Suosikkien luokalle muuttuja
     */
    private static Favorites favorites = new Favorites();
    /**
     * Viimeisin kaupunki
     */
    private String recentCity = "";
    /**
     * Nykyinen päänäkymän kaupunki
     */
    private String currentCity = "";
    /**
     * Yläreunan hakukenttä
     */
    private TextField searchBar;
    /**
     *  Alustetaan tiedoston käsittelijä tiedostonlukua varten
     */
    private ReadAndWriteToFile dataHandler;
    /**
     * Virheilmoitus virheellisistä hauista
     */
    private Label errorMessage;
    
    /**
     * Alustaa sovellusikkunan ja sen käyttöliittymäkomponentit.
     * @param stage sovelluksen ensisijainen näyttämö
     */
    @Override
    public void start(Stage stage) {
        // Alustetaan datan käsittelijä tiedostosta lukua varten.
        dataHandler = new ReadAndWriteToFile(favorites, recentCity);
        
        // Luetaan dataa tiedostosta
        try {
            recentCity = dataHandler.readFromFile("HistoryData.json");
        } 
        catch (Exception e) {
            System.err.println("Virhe tiedoston lukemisessa: " + e.getMessage());
        }
        
        
        this.stage = stage;
        // Luodaan uusi BorderPane päänäytölle
        this.root = new BorderPane();
        this.root.setPadding(new Insets(10, 10, 10, 10));
        
       // Määritellään taustakuvan URL
        String backgroundImage = "https://cdn.pixabay.com/photo/2017/08/31/14/40/cloud-2700931_1280.jpg";
        this.root.setStyle("-fx-background-image: url('" + backgroundImage + 
                "'); -fx-background-size: cover;");
        
        
        // Aloituskaupunkina Tampere paitsi jos recentCity:lta löytyy arvo.
        if ("".equals(recentCity)) {
            currentCity = "Tampere";
        } else {
            currentCity = recentCity;
        }
        
        // Piirretään pääsivu halutun kaupungin mukaisesti
        if(getWeatherData(currentCity)) {
            this.root.setCenter(getMainScreen(currentCity)); 
        }
        

        // Luodaan lopetuspainike sovellukselle ja sijoitetaan se alareunaan
        var quitButton = getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(10, 10, 10, 10));
        this.root.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        
        // Lisätään näkymän yläreunaan hakukenttä, -painike ja painike Suosikit
        // sivulle
        var favoritesButton = getFavoritesButton();
        this.searchBar = getSearchTextField();
        HBox searchBoxH = new HBox(10, this.searchBar, getSearchButton(), favoritesButton);
        searchBoxH.setAlignment(Pos.CENTER);
        
        // Lisätään hakukenttään linkitetty virheilmoitus
        this.errorMessage = new Label("");
        this.errorMessage.setStyle("-fx-font-size: 16px; " +
                       "-fx-font-family: Garamond; ");
        
        // Sijoitetaan HBox ja errorMessage näkymän yläreunaan
        VBox searchBoxV = new VBox(10);
        searchBoxV.getChildren().addAll(searchBoxH, this.errorMessage);
        searchBoxV.setAlignment(Pos.CENTER);
        BorderPane.setMargin(searchBoxV, new Insets(10, 10, 10, 10));
        this.root.setTop(searchBoxV);
        BorderPane.setAlignment(searchBoxV, Pos.TOP_CENTER);
        

        //Ensimmäinen skene ohjelman ikkunalle
        scene = new Scene(this.root, 500, 700);
        stage.setScene(scene);
        stage.setTitle("MitäsSää?");
        

        //Borderpane ja uusi skene toiselle näkymälle
        this.root2 = new BorderPane();
        this.root2.setPadding(new Insets(10, 10, 10, 10));
        scene2 = new Scene(this.root2, 500, 700);
        
        
        
        // Määritellään taustakuva Suosikit näkymälle
        this.root2.setStyle("-fx-background-image: url('" + backgroundImage + "'); -fx-background-size: cover;");
        
        
        // Päänäkymän Suosikit-painike vaihtaa suosikit näkymään
        favoritesButton.setOnAction((event) -> {
            stage.setScene(scene2);    
        });
        
        // Suosikitnäkymä
        // Sijoitetaan Suosikit näkymän VBox BorderPaneen
        this.root2.setCenter(getfavoritesScreen());
        
        // Suosikit skene sisältää painikkeet poistumiseen ja palaamiseen 
        // päänäkymään
        var quitButton2 = getQuitButton();
        BorderPane.setMargin(quitButton2, new Insets(10, 10, 10, 10));
        this.root2.setBottom(quitButton2);
        BorderPane.setAlignment(quitButton2, Pos.TOP_RIGHT);
        
        var backButton = getBackButton();
        BorderPane.setMargin(backButton, new Insets(10, 10, 10, 10));
        this.root2.setTop(backButton);
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
        
        // Suosikit näkymässä takaisin-painike vaihtaa päänäkymään
        backButton.setOnAction((event) -> {
            stage.setScene(scene);
        });
        
        stage.show();
    }

    public static void main(String[] args) throws ParseException {
        launch();
    }
    
    /**
     * Asettaa säätietoja pääikkunan näkymään.
     * @param city kaupunki, jonka tietoja esitetään
     * @return VBox, joka sisältää kaikki pääikkunassa esitettävät säätiedot
     */
    private VBox getMainScreen(String city) {
        
        // Luodaan kaupungin nimelle ja suosikkinapille komponentit
        Label cityName = new Label(""); 
        cityName.setText(city);
        cityName.setStyle("-fx-font-weight: bold; " +
                       "-fx-font-size: 32px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: white;");
        Button favorite = getAddToFavoritesButton();
        
        // Luodaan komponenteille VBox ja lisätään ne sinne
        HBox titleHBox = new HBox(10, cityName, favorite);
        titleHBox.setAlignment(Pos.CENTER);
        VBox titleBox = new VBox(titleHBox);
        titleBox.setPrefHeight(90);
        titleBox.setAlignment(Pos.CENTER);
        
        // Luodaan komponentit nykyisen sään tiedoille
        Label temp = new Label(String.valueOf(this.cw.getTemp()) + " °C");
        temp.setStyle("-fx-font-weight: bold; " +
                       "-fx-font-size: 32px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: gray;");
        
        Label feelsLike = new Label("Feels like: " + 
                String.valueOf(this.cw.getFeelsLike()) + " °C");
        feelsLike.setStyle("-fx-font-size: 24px; " +
                       "-fx-font-family: Garamond; ");
        
        ImageView weatherIcon = new ImageView(icons.getWeatherIcon(this.cw.getId()));
        weatherIcon.setFitHeight(100);
        weatherIcon.setFitWidth(100);
        
        Label wind = new Label("Tuuli: " + this.cw.getWind() + " m/s");
        wind.setStyle("-fx-font-size: 24px; " +
                       "-fx-font-family: Garamond; ");
        
        Label rainAmount = new Label("Sade: " + this.cw.getRain() + " mm");
        rainAmount.setStyle("-fx-font-size: 24px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: blue;");
        
        // Luodaan säätiedoille oma VBox
        HBox currentBox = new HBox(10);
        VBox currentBox1= new VBox(10, weatherIcon);
        VBox currentBox2 = new VBox(10, temp, feelsLike, wind, rainAmount);
        currentBox.getChildren().addAll(currentBox1, currentBox2);
        currentBox.setPrefHeight(180);
        currentBox.setAlignment(Pos.CENTER);
           
        // Luodaan tunti- ja vuorokausiennusteille Labelit
        Label hourTitle = new Label("TUNTIENNUSTE");
        hourTitle.setStyle("-fx-font-weight: bold; " +
                       "-fx-font-size: 20px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: white;");

        Label dayTitle = new Label("VUOROKAUSIENNUSTE"); 
        dayTitle.setStyle("-fx-font-weight: bold; " +
                       "-fx-font-size: 20px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: white;");
        
        // Luodaan tuntiennusteelle VBox
        VBox hourly = new VBox(10, hourTitle);
        hourly.setPrefHeight(165);
        hourly.setStyle("-fx-border-radius: 10;" +  
                      "-fx-border-width: 2px;");
        hourly.setAlignment(Pos.CENTER);
        
        // Luodaan HBox tuntiennusteille ja sille scrollPane
        HBox SHBox = new HBox();
        SHBox.setPrefHeight(120);
        SHBox.setAlignment(Pos.CENTER);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinHeight(90);
        scrollPane.setStyle("-fx-background: #b3d9f5; ");
        
        // Luodaan HBox, johon lisätään tuntikohtaiset VBoxit
        HBox VBoxesH = new HBox(10);
        VBoxesH.setAlignment(Pos.CENTER);
        
        // Luodaan tuntikohtaiselle säädatalle jokaiselle tunnille omat kompo-
        // nentit ja lisätään kaupungin säädata niihin
        // Säädatan läpikäynti aloitetaan kolmannesta indeksistä, jotta tunnit
        // saadaan alkamaan nykyisestä tunnista
        for (int i = 2; i < this.hourlyWeatherMap.size(); i++) {
            Label time = new Label(this.hourlyWeatherMap.get(i).getDate()
                    .substring(11, 16));
            time.setStyle("-fx-font-weight: bold; ");
            
            Label tempH = new Label(this.hourlyWeatherMap.get(i).getTemp() 
                    + " °C");
            
            Label feelsLikeH = new Label(this.hourlyWeatherMap.get(i)
                    .getFeelsLike() + " °C");
            feelsLikeH.setStyle("-fx-font-size: 10px; " +
            "-fx-text-fill: gray;");
            
            ImageView iconH = new ImageView(icons.getWeatherIcon(this
                    .hourlyWeatherMap.get(i).getId()));
            iconH.setFitHeight(30);
            iconH.setFitWidth(30);
            
            Label rainProbH = new Label(this.hourlyWeatherMap.get(i).getPop()
                    + " %");
            rainProbH.setStyle("-fx-text-fill: blue;");
            
            // Luodaan VBox ja lisätään tuntikomponentit siihen
            VBox h = new VBox(time, tempH, feelsLikeH, rainProbH, iconH);
            h.setStyle("-fx-border-radius: 10;" +
            "-fx-border-color: white; " +
            "-fx-border-width: 2px;");
            h.setMinWidth(55);
            h.setAlignment(Pos.CENTER);
            VBoxesH.getChildren().add(h);
        }
        
        // Lisätään for-loopilla luodut VBoxit ScrollPaneen
        hourly.getChildren().add(VBoxesH);
        scrollPane.setContent(VBoxesH);
        
        SHBox.getChildren().add(scrollPane);
        
        hourly.getChildren().add(SHBox);
        
        // Luodaan VBox päivittäiselle ennusteelle
        VBox daily = new VBox(10, dayTitle);
        daily.setPrefHeight(100);
        daily.setStyle("-fx-border-radius: 10;" +  
                      "-fx-border-width: 2px;");
        daily.setAlignment(Pos.CENTER);
        
        // Tehdään myös päiväkohtaisille ennustuksille HBox
        HBox VBoxesD = new HBox(10);
        VBoxesD.setAlignment(Pos.CENTER);
        
        // Luodaan päivittäiselle säädatalle omat komponentit ja lisätään
        // säädata niihin
        for (int j = 0; j < this.forecastDataMap.size(); j++) {
            Label date = new Label(this.forecastDataMap.get(j).getDate());
            date.setStyle("-fx-font-weight: bold; ");
            
            Label dayTemp = new Label(this.forecastDataMap.get(j).getTemp_day() 
                    + " °C");
            dayTemp.setStyle("-fx-text-fill: #fff200;");
            
            Label nightTemp = new Label(this.forecastDataMap.get(j)
                    .getTemp_night() + " °C");
            nightTemp.setStyle("-fx-text-fill: #000070;");
            
            Label rainProbD = new Label(this.forecastDataMap.get(j).getPop() 
                    + " %");
            rainProbD.setStyle("-fx-text-fill: blue;");
            
            ImageView iconD = new ImageView(icons.getWeatherIcon(this
                    .forecastDataMap.get(j).getId()));
            iconD.setFitHeight(30);
            iconD.setFitWidth(30);
            
            // Luodaan VBox ja lisätään päiväennusteen komponentit siihen
            VBox d = new VBox(date, dayTemp, nightTemp, rainProbD, iconD);
            d.setStyle("-fx-border-radius: 10;" +
                "-fx-border-color: white; " +  
                      "-fx-border-width: 2px;");
            d.setMinWidth(55);
            d.setAlignment(Pos.CENTER);
            VBoxesD.getChildren().add(d);
        }
        
        // Lisätään for-loopilla tehdyt Boxit päivittäiseen ennustukseen
        daily.getChildren().add(VBoxesD);      
        
        // Lisätään kaikkien osioiden VBoxit yhteen VBoxiin
        VBox mainVBox = new VBox(8);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(titleBox, currentBox, hourly, daily);
        
        return mainVBox;                                        
    }
    
    /**
     * Asettaa kaupunkien säätietoja Suosikit-näkymään.
     * @return VBox, joka sisältää suosikit sivulle tarpeelliset tiedot
     */
    private VBox getfavoritesScreen() {  
        
        // Luodaan VBox, johon tiedot suosikeista sijoitetaan
        VBox favoritesBox = new VBox(10);
        
        // Jos suosikeita ei ole, asetetaan tästä tiedottava teksti
        if (favorites.getFavorites().isEmpty()) {
            Label noFavorites = new Label("Ei vielä suosikeita");
            noFavorites.setStyle("-fx-font-size: 20px; " +
                       "-fx-font-family: Garamond; " +
                       "-fx-text-fill: black;");
            favoritesBox.getChildren().add(noFavorites);  
        }
        else {
            // Luodaan jokaiselle suosikille oma nappi, joka näyttää tarpeelliset
            // tiedot kaupungeista ja niiden säästä
            for (int i = 0; i < favorites.getFavorites().size(); i++) {

                // Luodaan nappi kaupungille
                Button cityButton = new Button();
                cityButton.setMinWidth(460);
                cityButton.setStyle("-fx-background-color: rgba(0, 0, 0, 0);" +
                        "-fx-min-height: 60px;");

                // Luodaan komponentit kaupungille, lämpötilalle ja sääikonille
                String cityNameString = favorites.getFavorites().get(i);
                Text cityName = new Text(cityNameString);
                cityName.setFont(Font.font("Garamond", FontWeight.BOLD, 30));
                cityName.setStyle("-fx-fill: white;");

                getWeatherData(cityNameString);
                Text temp = new Text(this.cw.getTemp() + " °C");
                temp.setFont(Font.font("Garamond", 24));

                ImageView weatherIcon = new ImageView(icons.getWeatherIcon(this
                        .cw.getId()));
                weatherIcon.setFitHeight(100);
                weatherIcon.setFitWidth(100);

                // Lisätään nappiin HBoxin avulla ylläolevat tiedot
                HBox buttonTexts = new HBox(20);
                buttonTexts.setAlignment(Pos.CENTER_LEFT);
                buttonTexts.getChildren().addAll(cityName, temp, weatherIcon);
                cityButton.setGraphic(buttonTexts);

                // Lisätään kuuntelija nappiin, jonka avulla päästään kyseisen 
                // kaupungin säänäkymään
                cityButton.setOnAction(event -> {
                    // Asetetaan virheilmoituksen Label tyhjäksi
                    this.errorMessage.setText("");
                    
                    stage.setScene(scene);
                    if(getWeatherData(cityNameString)) {
                        updateScreen(cityNameString);
                    }
                });
                favoritesBox.getChildren().add(cityButton);
            }
        }
        // Luodaan VBox, johon lisätään kaikki sivulle tarpeellinen tieto
        VBox vBox = new VBox();
        
        // Luodaan scrollPane suosikkeja varten ja lisätään se sivun sisältöön
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: #b3d9f5; ");
        scrollPane.setContent(favoritesBox);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        vBox.getChildren().add(scrollPane);

        return vBox;
    }
    
    /**
     * Nappi, jonka avulla ohjelma suljetaan.
     * @return lopetusnappi
     */
    private Button getQuitButton() {
        Button button = new Button("Lopeta");
        button.setStyle("-fx-background-color: #b3d9f5;");

        // Lisätään nappiin kuuntelija, jonka avulla ohjelma lopetetaan
        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        return button;
    }
    
    /**
     * Tekstikenttä kaupunkien etsimistä varten.
     * @return tekstikenttä
     */
    private TextField getSearchTextField() {
        TextField textField = new TextField();
        
        // Lisätään kuuntelija TextFieldiin, joka tallentaa tekstiä muuttujaan 
        // aina kun teksti muuttuu
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.searchText = newValue; // Tallennetaan uusi teksti muuttujaan
        });       
       return textField;
    }
    
    /**
     * Nappi, jonka avulla kaupunkeja voidaan hakea
     * @return etsintänappi
     */
    private Button getSearchButton() {
        Button button = new Button();
        
        // Lisätään nappiin suurennuslasin kuva
        File magnifyingGlass = new File("icons/magnifyingGlass.png");
        Image image = new Image(magnifyingGlass.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        button.setGraphic(imageView);

        // Lisätään nappiin kuuntelija, jonka avulla etsitään kaupunkien
        // säätietoja
        button.setOnAction((ActionEvent event) -> {
            
            // Merkitään virheviestin teksti aina haun alussa tyhjäksi
            this.errorMessage.setText("");
            
            // Haetaan säätiedot ja päivitetään näkymä
            if(getWeatherData(this.searchText)) {
                updateScreen(this.searchText);
                this.searchBar.clear();
            }
        });
        
        button.setStyle("-fx-background-color: #b3d9f5;");
        
        return button;
    }
    
    /**
     * Nappi, jonka avulla päästään suosikkisivulle
     * @return suosikkinappi
     */
    private Button getFavoritesButton() {
        Button button = new Button("Suosikit");
        button.setStyle("-fx-background-color: #b3d9f5;");

        return button;
    }
    
    /**
     * Nappi, jonka avulla päästään suosikkisivulta takaisin pääsivulle
     * @return paluunappi
     */
    private Button getBackButton() {
        Button button = new Button();
        
        // Lisätään nappiin nuolen kuva
        File backArrow = new File("icons/backArrow.png");
        Image image = new Image(backArrow.toURI().toString());
        
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: #b3d9f5;");
        
        return button;
    }
    
    /**
     * Nappi, jonka avulla lisätään ja poistetaan suosikeita
     * @return lisäys- ja poistonappi
     */
    private Button getAddToFavoritesButton() {
        Button button = new Button();
        Image image;
        
        // Jos kaupunki on suosikeissa napissa on kokonaan väritetty tähti
        if (favorites.getFavorites().contains(this.currentCity)) {
            File fullStar = new File("icons/fullStar.png");
            image = new Image(fullStar.toURI().toString());
            
            // Lisätään nappiin kuuntelija, jonka avulla kaupunki poistetaan
            // suosikeista
            button.setOnAction((ActionEvent event) -> {
                favorites.removeFavorite(currentCity);
                
                // Päivitetään etu- ja suosikkisivun näkymät
                VBox main = getMainScreen(currentCity);
                this.root.setCenter(main);
                
                VBox favoriteScreen = getfavoritesScreen();
                this.root2.setCenter(favoriteScreen);    
            });           
        } 
        // Muuten napissa on vain ääriviivat sisältävä tähti
        else {
            File emptyStar = new File("icons/emptyStar.png");
            image = new Image(emptyStar.toURI().toString());

            // Lisätään kuuntelija, jolla kaupunki lisätään suosikkeihin
            button.setOnAction((ActionEvent event) -> {
                favorites.setFavorite(currentCity);

                // Päivitetään näkymät
                VBox main = getMainScreen(currentCity);
                root.setCenter(main);
                
                VBox favoriteScreen = getfavoritesScreen();
                root2.setCenter(favoriteScreen);     
            }); 
        }
        
        // Kirjoitetaan muutokset suosikkeihin tiedostoon talteen
        try {
            dataHandler.writeToFile("HistoryData.json", favorites, recentCity);
        } 
        catch (Exception e) {
            System.err.println("Virhe tiedostoon kirjoittaessa: " + e.getMessage());
        }
        
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: #b3d9f5;");
 
        return button;        
    }
    
    /**
     * Hakee säätiedot tietylle kaupungille.
     * @param city kaupunki, jonka säätietoja haetaan
     * @return tosi, jos tietojen hakeminen onnistuu, muuten epätosi
     */
    private boolean getWeatherData(String city) {
        
        // Tarkistetaan onnistuuko kaupungin koordinaattien haku
        Coordinates c = API.lookUpLocation(city);
        if(c == null) {
            this.errorMessage.setText("Hakemaasi kaupunkia ei löydy "
                    + "tietovarastosta");
            this.searchBar.clear();
            return false;
        }
        else {
            // Haetaan tarvittavat säätiedot kaupungille
            this.cw = API.getCurrentWeather(c.getLat(), c.getLon());
            this.forecastDataMap = API.getDailyForecast(city);
            this.hourlyWeatherMap = API.getHourlyForecast(c.getLat(), 
                    c.getLon());
            
            // Jos joidenkin tietojen haku ei onnistu, ilmoitetaan siitä käyt-
            // täjälle
            if(this.cw == null || this.forecastDataMap == null || 
                    this.hourlyWeatherMap == null) {
                this.errorMessage.setText("Hakemaasi kaupunkia ei löydy "
                        + "tietovarastosta");
                this.searchBar.clear();
                return false;
            }
            return true; 
        }
    }
    
    /**
     * Päivittää näytöllä näkyvät tiedot tarvittaessa.
     * @param city kaupunki, jonka tietoja halutaan näkyviin
     */
    private void updateScreen(String city) {
        
        // Merkitään haluttu kaupunki nykyiseksi kaupungiksi
        this.currentCity = city;
            
        // Päivitetään pääikkunan näkymä
        VBox main = getMainScreen(this.currentCity);
        this.root.setCenter(main);
        
        // Päivitetään viimeisimmän kaupungin arvo
        this.recentCity = this.currentCity;

        // Kirjoitetaan muutokset tiedostoon talteen
        try {
            dataHandler.writeToFile("HistoryData.json", favorites, recentCity);
        } 
        catch (Exception e) {
            System.err.println("Virhe tiedostoon kirjoittaessa: " + e.getMessage());
        }
    }
}