package com.example.trinkspiel;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trinkspiel.gamepackages.GamePackageStandard;

import java.util.ArrayList;
import java.util.Collections;

public class GameLoop extends AppCompatActivity {
    TextView aufgabe;
    GamePackageStandard gamePack = new GamePackageStandard();
    ArrayList<String> cards;
    ArrayList<String> players;
    int cardIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);
        aufgabe = findViewById(R.id.AufgabenTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        players = GroupPage.getPlayerList();
        cards = gamePack.getCards();
        shuffleInRandomOrder(cards);
        cardIndex = 0;
    }

    public void changeCard(View v){
        String aktuelleAufgabe = cards.get(cardIndex);
        String randomPlayer = players.get(getRandomNumber(0,players.size()));
        aktuelleAufgabe = aktuelleAufgabe.replace("$Sp1", randomPlayer);
        aufgabe.setText(aktuelleAufgabe);
        cardIndex++;
        if (cardIndex == cards.size()){
            cardIndex = 0;
            shuffleInRandomOrder(cards);
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void shuffleInRandomOrder(ArrayList<String> liste){
        Collections.shuffle(liste);
    }

}