package de.saufapparat.trinkspiel;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import de.saufapparat.trinkspiel.R;

import de.saufapparat.trinkspiel.util.Card;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import de.saufapparat.trinkspiel.util.Kategorie;

import java.util.ArrayList;
import java.util.Collections;

public class GameLoop extends AppCompatActivity {

    private TextView aufgabe;
    private ConstraintLayout mainLayout;
    private  ArrayList<Card> cards;
    private ArrayList<String> players;
    private int cardIndex;
    private boolean touchedRightHalf;
    private TextView schluckCount;
    private TextView schluckName;
    private TextView kategorieLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_loop);

        initializeViews();

        mainLayout.setOnTouchListener(new CardChangeListener());
    }

    private void initializeViews() {
        aufgabe = findViewById(R.id.AufgabenTextView);
        mainLayout = findViewById(R.id.mainLayout);
        schluckCount = findViewById(R.id.schluckCount);
        schluckName = findViewById(R.id.schluckName);
        kategorieLabel = findViewById(R.id.KategorieLabel);
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
            changeCard();
            return false;
        }
    }

    private int FetchDynamicScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.fullScreencall(this);

        shuffleCardsFillWithPlayersAnSetIndexToZero();
        changeCard();
    }

    private void shuffleCardsFillWithPlayersAnSetIndexToZero() {
        cards = fetchCardsFromProperties();
        shuffleInRandomOrder(cards);
        players = GroupSelectionPage.getPlayerList();
        fillCardsWithPlayers();
        cardIndex = 0;
    }

    private ArrayList<Card> fetchCardsFromProperties() {
        return GamePackageManager.getCardsFromProperties(this, PackageSelectionPage.getSelectedPackage(),getResources().getConfiguration().locale.getLanguage());
    }

    private void shuffleInRandomOrder(ArrayList<Card> list){
        Collections.shuffle(list);
    }

    private void fillCardsWithPlayers(){
        ArrayList<Card> temporaryCardDeck = cards;
        ArrayList<String> temporaryPlayers = players;
        String removedPlayer = null;

        for(int i = 0; i < cards.size(); i++){
            String randomPlayer = temporaryPlayers.get(getRandomNumber(0,temporaryPlayers.size()));
            temporaryPlayers.remove(randomPlayer);
            if(removedPlayer != null){
                temporaryPlayers.add(removedPlayer);
            }
            removedPlayer = randomPlayer;
            String randomPlayer2 = temporaryPlayers.get(getRandomNumber(0,temporaryPlayers.size()));

            String TaskWithPlayerReplaced = temporaryCardDeck.get(i).getAufgabe().replace("$Sp1", randomPlayer);
            TaskWithPlayerReplaced = TaskWithPlayerReplaced.replace("$Sp2", randomPlayer2);
            temporaryCardDeck.get(i).setAufgabe(TaskWithPlayerReplaced);

        }
        temporaryPlayers.add(removedPlayer);
        cards = temporaryCardDeck;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void changeCard() {
        NavigateThroughCards();
        changeViewDependingOfCard();
    }

    private void NavigateThroughCards() {
        if (touchedRightHalf) {
            cardIndex++;
            if (cardIndex == cards.size()) {
                shuffleCardsFillWithPlayersAnSetIndexToZero();
                Toast.makeText(this, getString(R.string.karten_gemischt), Toast.LENGTH_SHORT).show();
            }
        } else {
            cardIndex--;
            if (cardIndex < 0) {
                cardIndex = 0;
            }
        }
    }

    private void changeViewDependingOfCard() {
        prepareAktuelleAufgabe();
        prepareAktuelleSchlucke();
        changeViewDependingOnKategorie();
    }

    private void prepareAktuelleAufgabe() {
        aufgabe.setText(getAufgabeFromCardWithIndex());
    }

    private String getAufgabeFromCardWithIndex() {
        return cards.get(cardIndex).getAufgabe();
    }

    private void prepareAktuelleSchlucke() {
        int aktuelleSchlucke = getSchluckeFromCardWithIndex();
        schluckCount.setText("" + aktuelleSchlucke);
        schluckCount.setVisibility(View.VISIBLE);
        schluckName.setVisibility(View.VISIBLE);
        if (aktuelleSchlucke == 0){
            schluckCount.setVisibility(View.INVISIBLE);
            schluckName.setVisibility(View.INVISIBLE);
        }
    }

    private int getSchluckeFromCardWithIndex(){
        return cards.get(cardIndex).getSchlucke();
    }

    private void changeViewDependingOnKategorie() {
        Kategorie aktuelleKategorie = getKategorieFromCardWithIndex();
        kategorieLabel.setText(aktuelleKategorie.getKategorieName());
        String color = aktuelleKategorie.getKategorieColorName();
        changeBackground(color);
    }

    private Kategorie getKategorieFromCardWithIndex() {
        return cards.get(cardIndex).getKategorie();
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
        aufgabe.setTextColor(color);
        schluckCount.setTextColor(color);
        schluckName.setTextColor(color);
        kategorieLabel.setTextColor(color);
    }

    public void backToPackages(View view){
        this.finish();
        return;
    }

}