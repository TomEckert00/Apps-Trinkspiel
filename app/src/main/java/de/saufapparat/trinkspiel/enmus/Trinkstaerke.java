package de.saufapparat.trinkspiel.enmus;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.saufapparat.trinkspiel.R;

public enum Trinkstaerke {

    entspannt(0.5),
    normal(1),
    stark(2),
    extrem(3),
    hardcore(5);

    private double multiplier;

    Trinkstaerke(double multiplier){
        this.multiplier = multiplier;
    }

    public static List<String> getValuesWithLanguage(String language, Context context){
        updateConfiguration(language, context);
        Resources res = context.getApplicationContext().getResources();
        List<String> result = new ArrayList<>();


        result.add(res.getString(R.string.mul_entspannt));
        result.add(res.getString(R.string.mul_normal));
        result.add(res.getString(R.string.mul_stark));
        result.add(res.getString(R.string.mul_extrem));
        result.add(res.getString(R.string.mul_hardcore));

        return result;
    }

    public double getMultiplier(){
        return this.multiplier;
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
