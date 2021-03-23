package com.example.trinkspiel.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.ArraySet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class GamePackageManager{

    public static ArrayList<String> getCardsFromProperties(Context context, String packagename, String language){
        System.out.println(language);
        System.out.println(packagename);
        ArrayList<String> cards = new ArrayList<>();
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(packagename + "Cards.properties");
            properties.load(inputStream);
        }catch (Exception e){}

        int size = Integer.parseInt(properties.getProperty("cardSetSize"));
        for(int i=1; i <= size; i++){
            cards.add(properties.getProperty(language + ".card." + i));
        }
        return cards;
    }

}
