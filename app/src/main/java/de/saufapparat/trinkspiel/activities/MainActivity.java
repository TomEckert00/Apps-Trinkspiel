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
import java.util.List;
import java.util.Locale;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.enmus.Trinkstaerke;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.MoreInformationPage;
import de.saufapparat.trinkspiel.util.TinyDB;

public class MainActivity extends AppCompatActivity{

    private ConstraintLayout mainView;
    private ConstraintLayout disclaimerView;
    private ImageView languageButton;
    private Button quickPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        mainView.setVisibility(View.GONE);
        checkDisclaimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.removeNavigationBarBottom(this);
    }

    private void initializeViews() {
        languageButton = findViewById(R.id.languageImageView);
        disclaimerView = findViewById(R.id.disclaimerView);
        mainView = findViewById(R.id.mainView);
        quickPlayButton = findViewById(R.id.quickplay);
        setImageButtonViewToLanguage(getResources().getConfiguration().locale.getLanguage());
        showQuickPlayIfPossible();
    }

    private void checkDisclaimer(){
        boolean disClaimerShown = getIntent().getBooleanExtra("disclaimerShown", false);
        if (disClaimerShown){
            killDisclaimer(null);
        }
    }

    public void killDisclaimer(View view){
        disclaimerView.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
    }

    public void openGroupSelectionPage(View view){
        Intent intent = new Intent(this, GroupSelectionPage.class);
        startActivity(intent);
    }

    public void quickPlay(View view){
        Intent intent = new Intent(this, GroupSelectionPage.class);
        intent.putExtra("quickplay", "true");
        startActivity(intent);
    }

    private void showQuickPlayIfPossible() {
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        ArrayList<String> spieler = tinyDB.getListString("spielerListe");
        GamePackage selectedPackage = tinyDB.getObject("selectedPackage", GamePackage.class);
        Trinkstaerke trinkstaerke = tinyDB.getObject("trinkstaerke", Trinkstaerke.class);
        GetraenkeTyp getraenkeTyp = tinyDB.getObject("getraenketyp", GetraenkeTyp.class);
        int cardindex = tinyDB.getInt("cardindex");
        ArrayList<Object> lis = tinyDB.getListObject("cards", Card.class);

        String savedLanguage = tinyDB.getString("savedLanguage");
        String actualLanguage = getResources().getConfiguration().locale.getLanguage();

        ArrayList<Card> newcards = new ArrayList<>();
        for(Object o : lis){
            newcards.add((Card) o);
        }
        ArrayList<Card> cards = newcards;
        if(spieler!=null && selectedPackage!=null && trinkstaerke!= null&&getraenkeTyp!=null&&cards.size()!=0&&actualLanguage.equals(savedLanguage)){
            quickPlayButton.setEnabled(true);
        }else {
            quickPlayButton.setEnabled(false);
        }
    }

    public void openInstagram(View view){
        Uri uri = Uri.parse("https://www.instagram.com/saufapparat/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openMoreInformationPage(View view){
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }
    
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