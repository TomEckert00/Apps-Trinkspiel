package de.saufapparat.trinkspiel.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.enmus.Trinkstaerke;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.model.Kategorie;
import lombok.Setter;

public class GamePackageManager{

    private static String sprache;

    @Setter
    public static Trinkstaerke trinkstaerke = Trinkstaerke.normal;
    @Setter
    public static GetraenkeTyp getraenkeTyp = GetraenkeTyp.schlucke;

    public static ArrayList<Card> getCardsFromProperties(Context context, String packagename, String language){
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        sprache = language;
        try {
            InputStream inputStream = assetManager.open(packagename + "Cards.properties");
            properties.load(inputStream);
        }catch (Exception e){}

        return cardsFromProperties(properties);
    }

    private static ArrayList<Card> cardsFromProperties(Properties properties) {
        ArrayList<Card> temporaryCards = new ArrayList<>();

        int cardSetSize = fetchCardSetSize(properties);
        if (cardSetSize > 0) {
            for (int index = 1; index <= cardSetSize; index++) {
                temporaryCards.add(buildNewCard(properties, index));
            }
        }else{
            temporaryCards.add(new Card("No Cards found",404, new Kategorie("kategorie","blau")));
        }
        return temporaryCards;
    }

    private static int fetchCardSetSize(Properties properties) {
        return Integer.parseInt(properties.getProperty("cardSetSize"));
    }

    private static Card buildNewCard(Properties properties, int index){
        String aufgabe = fetchAufgabe(properties, index);
        int schlucke = fetchSchlucke(properties, index);
        Kategorie kategorie = fetchKategorie(properties, index);
        return new Card(aufgabe, schlucke, kategorie);
    }

    private static String fetchAufgabe(Properties properties, int index) {
        String result = properties.getProperty(sprache + ".card." + index + ".aufgabe");
        return (result == null || result.isEmpty()) ? "Nothing to do" : result;
    }

    private static int fetchSchlucke(Properties properties, int index) {
        String result;
        if (getraenkeTyp.equals(GetraenkeTyp.schlucke)){
            result = properties.getProperty("card." + index + ".schlucke");
        }else{
            result = properties.getProperty("card." + index + ".shots");
        }
        return result == null ? 0 : (int) Math.round(Integer.parseInt(result) * trinkstaerke.getMultiplier());
    }

    private static Kategorie fetchKategorie(Properties properties, int index) {
        String kategorieNumber = properties.getProperty("card." + index + ".kategorie");
        String kategorieName = properties.getProperty(sprache + ".kategorie."+ kategorieNumber + ".name");
        String kategorieColor = properties.getProperty("kategorie."+kategorieNumber + ".color");
        return new Kategorie(kategorieName,kategorieColor);

    }
}
