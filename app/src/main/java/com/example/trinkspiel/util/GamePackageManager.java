package com.example.trinkspiel.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.provider.CalendarContract;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

import com.example.trinkspiel.R;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GamePackageManager{

    public static ArrayList<Card> getCardsFromProperties(Context context, String packagename, String language){
        System.out.println(language);
        System.out.println(packagename);
        ArrayList<Card> cards = new ArrayList<>();
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(language + "_" + packagename + "Cards.properties");
            properties.load(inputStream);
        }catch (Exception e){}

        List<Kategorie> kategories = new ArrayList();
        Kategorie nothing = new Kategorie("","WHITE");
        kategories.add(0,nothing);

        int kategorieCount = Integer.parseInt(properties.getProperty("kategorie.count"));
        for(int i=1 ; i<=kategorieCount ; i++){
            String kategorieName = properties.getProperty("kategorie."+ i + ".name");
            String kategorieColorName = properties.getProperty("kategorie." + i + ".color");
            Kategorie kategorie = new Kategorie(kategorieName,kategorieColorName);
            kategories.add(i,kategorie);
        }

        int size = Integer.parseInt(properties.getProperty("cardSetSize"));
        if (size > 0) {
            for (int i = 1; i <= size; i++) {
                String aufgabe = properties.getProperty(language + ".card." + i + ".aufgabe");
                int schlucke = Integer.parseInt(properties.getProperty("card." + i + ".schlucke"));
                String katego = properties.getProperty("card." + i + ".kategorie");
                if (katego == null){
                    katego = "0";
                }
                int kategorieIndex = Integer.parseInt(katego);
                Kategorie kategorieForCard = kategories.get(kategorieIndex);

                cards.add(new Card(i, aufgabe, schlucke,kategorieForCard));
            }
        }else{
            cards.add(new Card(1,"No Cards found",404, kategories.get(0)));
        }
        return cards;
    }
}
