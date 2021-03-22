package com.example.trinkspiel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
public class MainSettings extends AppCompatActivity {

    private String language = "de";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);
    }

    /*
    public void reloadForLanguage(View view){
        TextView tv = (TextView) view;
        language = tv.getText().toString();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        System.out.println(language);
        recreate();
    }
     */

    public void settingsMade(View view){
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("changedLanguage", language);
        startActivity(intent);
    }
}