package de.saufapparat.trinkspiel.enmus;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.saufapparat.trinkspiel.R;

public enum GetraenkeTyp {
    schlucke,
    shots;

    public static List<String> getValuesWithLanguage(String language, Context context){
        updateConfiguration(language, context);
        Resources res = context.getApplicationContext().getResources();
        List<String> result = new ArrayList<>();
        result.add(res.getString(R.string.gameLoop_Schlucke));
        result.add(res.getString(R.string.gameLoop_Shots));
        return result;
    }

    private static void updateConfiguration(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = context.getApplicationContext().getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}
