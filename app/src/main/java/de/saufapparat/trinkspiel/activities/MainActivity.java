package de.saufapparat.trinkspiel.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Locale;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.enmus.Trinkstaerke;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.MoreInformationPage;
import de.saufapparat.trinkspiel.util.TinyDB;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainView;
    private ConstraintLayout disclaimerView;
    private ImageView languageButton;
    private Button quickPlayButton;
    private String language;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        checkIfDisclaimerWasShown();
        setImageButtonViewToLanguage(language);
    }

    private void initializeViews() {
        languageButton = findViewById(R.id.languageImageView);
        disclaimerView = findViewById(R.id.disclaimerView);
        mainView = findViewById(R.id.mainView);
        quickPlayButton = findViewById(R.id.quickplay);
        language = fetchLanguageFromResources();
        tinyDB = new TinyDB(getApplicationContext());
    }

    private void checkIfDisclaimerWasShown() {
        mainView.setVisibility(View.GONE);
        boolean disClaimerShown = getIntent().getBooleanExtra("disclaimerShown", false);
        if (disClaimerShown) {
            killDisclaimer(null);
        }
    }

    public void killDisclaimer(View view) {
        disclaimerView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    private void showQuickPlayIfPossible() {
        ArrayList<String> savedPlayers = tinyDB.getListString("spielerListe");
        GamePackage savedPackage = tinyDB.getObject("selectedPackage", GamePackage.class);
        Trinkstaerke savedTrinkstaerke = tinyDB.getObject("trinkstaerke", Trinkstaerke.class);
        GetraenkeTyp savedGetraenketyp = tinyDB.getObject("getraenketyp", GetraenkeTyp.class);
        ArrayList<Object> savedCards = tinyDB.getListObject("cards", Card.class);
        String savedLanguage = tinyDB.getString("savedLanguage");
        String actualLanguage = fetchLanguageFromResources();

        ArrayList<Card> castedCards = castCards(savedCards);

        if (savedPlayers != null &&
                savedPackage != null &&
                savedTrinkstaerke != null &&
                savedGetraenketyp != null &&
                castedCards.size() != 0 &&
                actualLanguage.equals(savedLanguage)
        ) {
            quickPlayButton.setEnabled(true);
        } else {
            quickPlayButton.setEnabled(false);
        }
    }

    private ArrayList<Card> castCards(ArrayList<Object> lis) {
        ArrayList<Card> castedCards = new ArrayList<>();
        for (Object o : lis) {
            castedCards.add((Card) o);
        }
        return castedCards;
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.removeNavigationBarBottom(this);
        showQuickPlayIfPossible();
    }

    public void openGroupSelectionPage(View view) {
        Intent intent = new Intent(this, GroupSelectionPage.class);
        startActivity(intent);
    }

    public void quickPlay(View view) {
        Intent intent = new Intent(this, GroupSelectionPage.class);
        intent.putExtra("quickplay", "true");
        startActivity(intent);
    }

    public void openInstagram(View view) {
        Uri uri = Uri.parse("https://www.instagram.com/saufapparat/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openMoreInformationPage(View view) {
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }

    public void changeLanguageToNextInOrder(View view) {
        String language = fetchLanguageFromResources();
        if (language == "de") {
            language = "en";
        } else if (language == "en") {
            language = "fr";
        } else if (language == "fr") {
            language = "de";
        }
        setImageButtonViewToLanguage(language);
        reloadPageForNewLanguage(language);
    }

    private String fetchLanguageFromResources() {
        return getResources().getConfiguration().locale.getLanguage();
    }

    private void setImageButtonViewToLanguage(String language) {
        switch (language) {
            case "de":
                languageButton.setImageResource(R.drawable.deutschland);
                break;
            case "fr":
                languageButton.setImageResource(R.drawable.frankreich);
                break;
            case "en":
                languageButton.setImageResource(R.drawable.greatbritain);
                break;
        }
    }

    private void reloadPageForNewLanguage(String language) {
        UpdateConfiguration(language);
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("disclaimerShown", false);
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