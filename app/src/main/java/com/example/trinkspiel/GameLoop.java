package com.example.trinkspiel;

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


import com.example.trinkspiel.util.Card;
import com.example.trinkspiel.util.GamePackageManager;
import com.example.trinkspiel.util.Kategorie;

import java.util.ArrayList;
import java.util.Collections;

public class GameLoop extends AppCompatActivity {
    TextView aufgabe;
    ConstraintLayout mainLayout;
    ArrayList<Card> cards;
    ArrayList<String> players;
    int cardIndex;
    boolean touchedRightHalf;
    TextView schluckCount;
    TextView schluckName;
    TextView kategorieLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_loop);
        aufgabe = findViewById(R.id.AufgabenTextView);
        mainLayout = findViewById(R.id.mainLayout);
        schluckCount = findViewById(R.id.schluckCount);
        schluckName = findViewById(R.id.schluckName);
        kategorieLabel = findViewById(R.id.KategorieLabel);
        System.out.println(schluckCount.getText());
        mainLayout.setOnTouchListener(new View.OnTouchListener(){
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
        });
    }

    private int FetchDynamicScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    protected void onResume() {
        super.onResume();
        shuffleCardsFillWithPlayersAnSetIndexToZero();
        changeCard();
    }

    private void shuffleCardsFillWithPlayersAnSetIndexToZero() {
        cards = GamePackageManager.getCardsFromProperties(this,PackageSelectionPage.getSelectedPackage(),getResources().getConfiguration().locale.getLanguage());
        shuffleInRandomOrder(cards);
        players = GroupSelectionPage.getPlayerList();
        fillCardsWithPlayers();
        cardIndex = 0;
    }

    public void changeCard() {
        if (touchedRightHalf) {
            cardIndex++;
            if (cardIndex == cards.size()) {
                System.out.println("Letzte Karte erreicht, starte von vorne");
                shuffleCardsFillWithPlayersAnSetIndexToZero();
                Toast.makeText(this, "Deck shuffled", Toast.LENGTH_SHORT).show();
            }
        } else {
            cardIndex--;
            if (cardIndex < 0) {
                cardIndex = 0;
            }
        }
        String aktuelleAufgabe = cards.get(cardIndex).getAufgabe();
        aufgabe.setText(aktuelleAufgabe);
        int aktuelleSchlucke = cards.get(cardIndex).getSchlucke();
        schluckCount.setText(""+aktuelleSchlucke);
        schluckCount.setVisibility(View.VISIBLE);
        schluckName.setVisibility(View.VISIBLE);
        if (aktuelleSchlucke == 0){
            schluckCount.setVisibility(View.INVISIBLE);
            schluckName.setVisibility(View.INVISIBLE);
        }
        Kategorie aktuelleKategorie = cards.get(cardIndex).getKategorie();
        kategorieLabel.setText(aktuelleKategorie.getKategorieName());
        String color = aktuelleKategorie.getKategorieColorName();

        System.out.println("Aktuelle Kategorie "+ aktuelleKategorie.getKategorieName());
        System.out.println("Aktuelle Farbe "+ color);

        if(color.equals("RED")){
            mainLayout.setBackgroundColor(Color.RED);
        }
        else {
            mainLayout.setBackgroundColor(Color.WHITE);
        }
    }


    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void shuffleInRandomOrder(ArrayList<Card> list){
        Collections.shuffle(list);
    }

    private void fillCardsWithPlayers(){
        ArrayList<Card> kartenDeck = cards;
        ArrayList<String> spieler = players;
        String removedPlayer = null;
        int repetitions = cards.size();
        for(int i=0;i<repetitions;i++){
            String randomPlayer = spieler.get(getRandomNumber(0,spieler.size()));
            spieler.remove(randomPlayer);
            if(removedPlayer != null){
                spieler.add(removedPlayer);
            }
            removedPlayer = randomPlayer;
            String anotherPlayer = spieler.get(getRandomNumber(0,spieler.size()));

            String TaskWithPlayerReplaced = kartenDeck.get(i).getAufgabe().replace("$Sp1", randomPlayer);
            TaskWithPlayerReplaced = TaskWithPlayerReplaced.replace("$Sp2", anotherPlayer);
            kartenDeck.get(i).setAufgabe(TaskWithPlayerReplaced);



        }
        spieler.add(removedPlayer);
        cards = kartenDeck;
        System.out.println("Neues Deck:");
        System.out.println(cards.toString());
    }


    public void backToPackages(View view){
        this.finish();
        return;
    }

}