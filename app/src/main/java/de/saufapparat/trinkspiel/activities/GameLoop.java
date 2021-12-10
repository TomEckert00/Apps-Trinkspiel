package de.saufapparat.trinkspiel.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.activities.GameConfigurationActivity;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.service.GameLoopService;
import de.saufapparat.trinkspiel.model.Card;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.model.Kategorie;
import lombok.Setter;

public class GameLoop extends AppCompatActivity {

    private TextView textview_aufgabe;
    private ConstraintLayout mainLayout;
    private boolean touchedRightHalf;
    private TextView textview_schluckCount;
    private TextView textview_schluckName;
    private TextView textview_kategorieLabel;
    private GameLoopService gameLoopService;
    private Card aktuelleKarte;

    private MediaPlayer mediaPlayer_hupe;

    @Setter
    public static GetraenkeTyp getraenkeTyp = GetraenkeTyp.schlucke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        initializeViews();

        mainLayout.setOnTouchListener(new CardChangeListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.removeNotchForFullScreen(this);
        HelperUtil.removeNavigationBarBottom(this);
    }

    private boolean gameLoopServiceBound;

    @Override
    protected void onStart() {
        super.onStart();
        startGameService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gameLoopServiceBound){
            unbindService(myConnection);
            gameLoopServiceBound = false;
        }
    }

    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            gameLoopService = ((GameLoopService.MyBinder) binder).getService();
            gameLoopServiceBound = true;
            showCard();
        }

        public void onServiceDisconnected(ComponentName className) {
            gameLoopServiceBound = false;
        }
    };

    private void startGameService() {
        Intent serviceIntent = new Intent(this, GameLoopService.class);
        String language = getResources().getConfiguration().locale.getLanguage();
        serviceIntent.putExtra("language", language);
        startService(serviceIntent);
        bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    private void initializeViews() {
        textview_aufgabe = findViewById(R.id.AufgabenTextView);
        mainLayout = findViewById(R.id.mainLayout);
        textview_schluckCount = findViewById(R.id.schluckCount);
        textview_schluckName = findViewById(R.id.schluckName);
        textview_kategorieLabel = findViewById(R.id.KategorieLabel);
    }

    private class CardChangeListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            int screenWidth = FetchDynamicScreenWidth();
            if(x >= screenWidth/2){
                touchedRightHalf = true;
            }
            else {
                touchedRightHalf = false;
            }
            navigateThroughCards();
            return false;
        }
    }

    private int FetchDynamicScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void navigateThroughCards(){
        if(touchedRightHalf){
            showNextCard();
        }
        else {
            showPreviousCard();
        }
    }

    private void showNextCard() {
        gameLoopService.nextCard();
        showCard();
    }

    private void showPreviousCard() {
        gameLoopService.previousCard();
        showCard();
    }

    private void showCard(){
        aktuelleKarte = gameLoopService.fetchCurrentCard();
        textview_aufgabe.setText(aktuelleKarte.getAufgabe());
        playSoundWhenActivityPackageSpezialActivated();
        showSchlueckeIfPossible();
        changeViewDependingOnKategorie();
    }

    private void playSoundWhenActivityPackageSpezialActivated() {
        if (PackageSelectionPage.getSelectedPackage().equals(GamePackage.ActivityPackage)
                && aktuelleKarte.getKategorie().getKategorieName().equals("Spezial")){
            if(mediaPlayer_hupe==null){
                mediaPlayer_hupe = MediaPlayer.create(this, R.raw.hupe_sound);
            }
            mediaPlayer_hupe.start();
        }
    }


    private void showSchlueckeIfPossible() {
        decideGetraenkeTyp();
        textview_schluckCount.setText("" + aktuelleKarte.getSchlucke());
        textview_schluckCount.setVisibility(View.VISIBLE);
        textview_schluckName.setVisibility(View.VISIBLE);
        if (aktuelleKarte.getSchlucke() == 0){
            textview_schluckCount.setVisibility(View.INVISIBLE);
            textview_schluckName.setVisibility(View.INVISIBLE);
        }
    }

    //todo in resourcen strings auslagern

    private void decideGetraenkeTyp() {
        if (getraenkeTyp.equals(GetraenkeTyp.schlucke)){
            textview_schluckName.setText(getString(R.string.gameLoop_Schlucke));
        }else if (getraenkeTyp.equals(GetraenkeTyp.shots)){
            textview_schluckName.setText(getString(R.string.gameLoop_Shots));
        }
    }

    private void changeViewDependingOnKategorie() {
        Kategorie aktuelleKategorie = aktuelleKarte.getKategorie();
        textview_kategorieLabel.setText(aktuelleKategorie.getKategorieName());
        String color = aktuelleKategorie.getKategorieColorName();
        changeBackground(color);
    }

    private void changeBackground(String color) {
        switch (color){
            case "Normal":
                mainLayout.setBackgroundColor(getResources().getColor(R.color.flo4));
                setViewColors(Color.WHITE);
                break;
            case "Rot":
                mainLayout.setBackgroundColor(Color.RED);
                setViewColors(Color.BLACK);
                break;
            case "Blau":
                mainLayout.setBackgroundColor(Color.BLUE);
                setViewColors(Color.WHITE);
                break;
            case "Grün":
                mainLayout.setBackgroundColor(Color.GREEN);
                setViewColors(Color.BLACK);
                break;
            case "Schwarz":
                mainLayout.setBackgroundColor(Color.BLACK);
                setViewColors(Color.WHITE);
                break;
            case "Gelb":
                mainLayout.setBackgroundColor(Color.YELLOW);
                setViewColors(Color.BLACK);
                break;
            case "Orange":
                mainLayout.setBackgroundColor(Color.parseColor("#FFA500"));
                setViewColors(Color.BLACK);
                break;
            default:
                mainLayout.setBackgroundColor(Color.WHITE);
                setViewColors(Color.BLACK);
                break;
        }
    }

    private void setViewColors(int color){
        textview_aufgabe.setTextColor(color);
        textview_schluckCount.setTextColor(color);
        textview_schluckName.setTextColor(color);
        textview_kategorieLabel.setTextColor(color);
    }

    //should finish Configpage as well if boolean is set to true
    public void backToPackageSelectionPage(View view){
        stopService(new Intent(this,GameLoopService.class));
        finish();
        GameConfigurationActivity.setConfigsSet(true);
        return;
    }

    public void backToConfigurations(View view){
        stopService(new Intent(this,GameLoopService.class));
        finish();
        return;
    }

}