package com.example.trinkspiel;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    public void reloadForLanguage(){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        recreate();
    }
     */

    public void openGroupPage(View v){
        Intent intent = new Intent(this, GroupPage.class);
        startActivity(intent);
    }

    public void openInstagram(View v){
        Uri uri = Uri.parse("https://www.instagram.com/tom_eckert_/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openMoreInformationPage(View v){
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }

    /*public void openMainSettings(View v){
        Intent intent = new Intent(this, MainSettings.class);
        startActivity(intent);
    }
     */

    public void changeLanguageToNextInOrder(View view){
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language == "de"){
            language = "en";
            System.out.println(language);
        }
        else if (language == "en"){
            language = "fr";
            System.out.println(language);
        }
        else if (language == "fr"){
            language = "de";
            System.out.println(language);
        }
        System.out.println(language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        recreate();
    }

}