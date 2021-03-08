package com.example.trinkspiel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GroupPage extends AppCompatActivity {

    private static ArrayList<String> playerList = new ArrayList<>();
    private LinearLayout playerListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        playerListLayout = findViewById(R.id.playerListLinearLayout);
    }

    public void openPackageSelectionPage(View v){
        playerList = new ArrayList<>();

        //Spielernamen zur Liste hinzufügen
        for (int index=0; index<playerListLayout.getChildCount()-1; index++){
            String name = ((EditText)playerListLayout.getChildAt(index)).getText().toString();
            if(!TextUtils.isEmpty(name)){
                playerList.add(name);
            }
        }

        //Validation
        int i=1;
        while(playerList.size() < 2){
            playerList.add("Neuer Spieler " + i);
            i++;
        }
        //opens next Intent
        Intent intent = new Intent(this, PackageSelectionPage.class);
        startActivity(intent);
    }

    public void addNewPlayerInput(View v){
        int lastIndex = playerListLayout.getChildCount()-1;
        EditText newField = new EditText(this);
        newField.setHint("Spieler " + (lastIndex+1));
        playerListLayout.addView(newField,lastIndex);
    }

    public static ArrayList<String> getPlayerList(){
        return playerList;
    }

}