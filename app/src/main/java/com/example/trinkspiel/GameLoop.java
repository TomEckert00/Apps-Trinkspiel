package com.example.trinkspiel;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.trinkspiel.gamepackages.AbstractGamePackage;
import com.example.trinkspiel.gamepackages.GamePackageStandard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class GameLoop extends AppCompatActivity {
    TextView aufgabe;
    ConstraintLayout mainLayout;
    AbstractGamePackage gamePack;
    ArrayList<String> cards;
    ArrayList<String> players;
    int cardIndex;
    boolean touchedRightHalf;
    private AbstractGamePackage selectedgamePackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_loop);
        aufgabe = findViewById(R.id.AufgabenTextView);
        mainLayout = findViewById(R.id.mainLayout);
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
        players = GroupPage.getPlayerList();
        fillCardsWithPlayers();
        cardIndex = 0;
    }

    public void changeCard(){
        if (touchedRightHalf){
            cardIndex++;
            if (cardIndex == cards.size()){
                System.out.println("Letzte Karte erreicht, starte von vorne");
                shuffleCardsFillWithPlayersAnSetIndexToZero();
            }
        }else{
            cardIndex--;
            if (cardIndex<0){
                cardIndex=0;
            }
        }
        String aktuelleAufgabe = cards.get(cardIndex);
        aufgabe.setText(aktuelleAufgabe);

    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void shuffleInRandomOrder(ArrayList<String> list){
        Collections.shuffle(list);
    }

    private void fillCardsWithPlayers(){
        ArrayList<String> kartenDeck = cards;
        ArrayList<String> spieler = players;
        String removedPlayer = null;
        int repetitions = cards.size();
        for(int i=0;i<repetitions;i++){
            String randomPlayer = spieler.get(getRandomNumber(0,spieler.size()));
            String TaskWithPlayerReplaced = kartenDeck.get(i).replace("$Sp1", randomPlayer);
            kartenDeck.set(i, TaskWithPlayerReplaced);
            spieler.remove(randomPlayer);
            if(removedPlayer != null){
                spieler.add(removedPlayer);
            }
            removedPlayer = randomPlayer;
        }
        spieler.add(removedPlayer);
        cards = kartenDeck;
        System.out.println("Neues Deck:");
        System.out.println(cards.toString());
    }

}