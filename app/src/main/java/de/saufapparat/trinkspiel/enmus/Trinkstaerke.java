package de.saufapparat.trinkspiel.enmus;


import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getValuesWithLanguage(Context context){
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
}
