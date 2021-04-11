package com.example.trinkspiel;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout mainView;
    ConstraintLayout disclaimerView;
    ImageView languageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        languageButton = findViewById(R.id.languageImageView);
        String currentlanguage = getResources().getConfiguration().locale.getLanguage();
        setImageButtonViewToLanguage(currentlanguage);
        boolean disClaimerShown = getIntent().getBooleanExtra("disclaimerShown", false);
        if (disClaimerShown){
            killDisclaimer(null);
        }
    }

    public void killDisclaimer(View view){
        disclaimerView = findViewById(R.id.disclaimerView);
        disclaimerView.setVisibility(View.GONE);
        mainView = findViewById(R.id.mainView);
        mainView.setVisibility(View.VISIBLE);
    }

    public void openGroupSelectionPage(View view){
        Intent intent = new Intent(this, GroupSelectionPage.class);
        startActivity(intent);
    }

    public void openInstagram(View view){
        Uri uri = Uri.parse("https://www.instagram.com/tom_eckert_/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openMoreInformationPage(View view){
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }

    /*public void openMainSettings(View v){
        Intent intent = new Intent(this, MainSettings.class);
        startActivity(intent);
    }
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void changeLanguageToNextInOrder(View view){
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language == "de"){
            language = "en";
        }
        else if (language == "en"){
            language = "fr";
        }
        else if (language == "fr"){
            language = "de";
        }
        setImageButtonViewToLanguage(language);
        reloadPageForNewLanguage(language);
    }

    private void setImageButtonViewToLanguage(String language){
        switch (language){
            case "de":
                languageButton.setImageResource(R.drawable.deutschland);
                break;
            case "fr":
                languageButton.setImageResource(R.drawable.frankreich);
                break;
            default:
                languageButton.setImageResource(R.drawable.greatbritain);
                break;
        }
    }

    private void reloadPageForNewLanguage(String language) {
        UpdateConfiguration(language);
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("disclaimerShown", true);
        startActivity(intent);
    }

    private void UpdateConfiguration(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}