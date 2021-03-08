package com.example.trinkspiel;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trinkspiel.gamepackages.GamePackageStandard;

import java.util.ArrayList;

public class GameLoop extends AppCompatActivity {
    TextView aufgabe;
    GamePackageStandard gamePack = new GamePackageStandard();
    ArrayList<String> cards;
    ArrayList<String> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);
        aufgabe = findViewById(R.id.AufgabenTextView);
        cards = gamePack.getCards();
    }

    public void changeCard(View v){
        players = GroupPage.getPlayerList();
        int randomIndex = getRandomNumber(0, cards.size());
        String aktuelleAufgabe = cards.get(randomIndex);
        String randomPlayer = players.get(getRandomNumber(0,players.size()));
        aktuelleAufgabe = aktuelleAufgabe.replace("$Sp1", randomPlayer);
        aufgabe.setText(aktuelleAufgabe);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}