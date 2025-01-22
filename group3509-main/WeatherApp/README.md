The project contains comments in Finnish and the following use guide is in Finnish also.
-
Ohjelman käynnistyessä se lukee HistoryData tiedostosta viimeisimmän haetun kaupungin ja asettaa
sen säätiedot ohjelmaan. Lisäksi suosikkeihin tallennetut kaupungit luetaan tiedostosta ja asetetaan
suosikit sivulle. Jos kuitenkaan historiaa ei ole, näytetään etusivulla Tampereen sää.
Eri kaupunkien säätietoja voidaan hakea kirjoittamalla kaupungin nimi ohjelman yläosasta löytyvään
tekstikenttään ja haku suoritetaan painamalla suurennuslasikuvakkeista hakunappia. Jos syötetty
kaupunki on virheellinen tai sen tietoja ei löydy, ilmoitetaan siitä käyttäjälle virheilmoituksella
hakukentän alapuolella.

Ohjelman etusivulla näkyy kaupungin tämänhetkinen sää, joka sisältää lämpötilan, lämpötilan
tuntuman, tuulen nopeuden ja sademäärän. Lisäksi säätä kuvaava sääikoni esitetään tietojen vieressä.
Etusivulla on myös tunti- ja päiväkohtainen ennuste. Tuntikohtaisessa ennusteessa näytetään
nykyisestä kellonajasta eteenpäin 24 tunnin ajalta säätiedot, joihin kuuluu päivä-ja yölämpötila, sateen
todennäköisyys ja säätä kuvaava ikoni. Vuorokausiennusteessa näytetään 7 päivän ennuste, joka
sisältää samat tiedot kuin tuntikohtaisissa ennuste.

Kaupunkeja voidaan lisätä suosikkeihin kaupungin nimen vierestä löytyvästä tähtikuvakkeen
sisältämästä napista ja suosikeista poistaminen tapahtuu samaa nappia painamalla. Napin tähti
sisältää vain reunat, jos se ei kuulu suosikkeihin ja on kokonaan täytetty, kun se on suosikeissa.
Suosikkeja pääsee selaamaan painamalla yläreunasta löytyvää Suosikit-nappia. Suosikit sivulla
suosikkeihin lisätyt kaupungit, ja niiden tämänhetkinen lämpötila ja säätä kuvaava ikoni, ovat
listattuna. Kaupunkien nimestä painamalla pääsee kyseisen kaupungin säätiedot näyttävälle
etusivulle. Suosikit sivulta pääsee takaisin etusivulle vasemman yläkulman nuolinappia painamalla.

My contribution to the project consisted of the implementation of getCurrentWeather in API.java, unifying 
of all the methods and combining the weather data into the graphical user interface.
