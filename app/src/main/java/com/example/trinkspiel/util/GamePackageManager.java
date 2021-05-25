package com.example.trinkspiel.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GamePackageManager{

    public static ArrayList<Card> getCardsFromProperties(Context context, String packagename, String language){
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(language + "_" + packagename + "Cards.properties");
            properties.load(inputStream);
        }catch (Exception e){}

        List<Kategorie> kategories = fetchKategories(properties);

        return cardsFromProperties(language, properties, kategories);
    }

    private static List<Kategorie> fetchKategories(Properties properties) {
        List<Kategorie> kategories = new ArrayList();
        addEmptyKategorie(kategories);
        addAllExistingKategories(properties, kategories);
        return kategories;
    }

    private static void addEmptyKategorie(List<Kategorie> kategories) {
        Kategorie nothing = new Kategorie("","WHITE");
        kategories.add(0, nothing);
    }

    private static void addAllExistingKategories(Properties properties, List<Kategorie> kategories) {
        int kategorieCount = Integer.parseInt(properties.getProperty("kategorie.count"));
        for(int i = 1 ; i <= kategorieCount; i++){
            addKategorieOfCard(properties, kategories, i);
        }
    }

    private static void addKategorieOfCard(Properties properties, List<Kategorie> kategories, int index) {
        String kategorieName = properties.getProperty("kategorie."+ index + ".name");
        String kategorieColorName = properties.getProperty("kategorie." + index + ".color");
        Kategorie kategorie = new Kategorie(kategorieName, kategorieColorName);
        kategories.add(index, kategorie);
    }

    private static ArrayList<Card> cardsFromProperties(String language, Properties properties, List<Kategorie> kategories) {
        ArrayList<Card> temporaryCards = new ArrayList<>();

        int cardSetSize = fetchCardSetSize(properties);
        if (cardSetSize > 0) {
            for (int i = 1; i <= cardSetSize; i++) {
                temporaryCards.add(buildNewCard(language, properties, kategories, i));
            }
        }else{
            temporaryCards.add(new Card("No Cards found",404, kategories.get(0)));
        }
        return temporaryCards;
    }

    private static int fetchCardSetSize(Properties properties) {
        return Integer.parseInt(properties.getProperty("cardSetSize"));
    }

    private static Card buildNewCard(String language, Properties properties, List<Kategorie> kategories, int index){
        String aufgabe = fetchAufgabe(language, properties, index);
        int schlucke = fetchSchlucke(properties, index);
        Kategorie kategorieForCard = FindCorrespondingKategorie(properties, kategories, index);
        return new Card(aufgabe, schlucke,kategorieForCard);
    }

    private static String fetchAufgabe(String language, Properties properties, int index) {
        return properties.getProperty(language + ".card." + index + ".aufgabe");
    }

    private static int fetchSchlucke(Properties properties, int index) {
        return Integer.parseInt(properties.getProperty("card." + index + ".schlucke"));
    }

    private static Kategorie FindCorrespondingKategorie(Properties properties, List<Kategorie> kategories, int index) {
        String kategorie = fetchKategorie(properties, index);
        int kategorieIndex = Integer.parseInt(kategorie);
        return kategories.get(kategorieIndex);
    }

    private static String fetchKategorie(Properties properties, int index) {
        String kategorie = properties.getProperty("card." + index + ".kategorie");
        return (kategorie == null) ? "0" : kategorie;
    }
}
