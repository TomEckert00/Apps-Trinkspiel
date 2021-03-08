package com.example.trinkspiel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PackageSelectionPage extends AppCompatActivity {

    private ArrayList<String> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);
        playerList = GroupPage.getPlayerList();
        TextView probeOutput = findViewById(R.id.playerListOutput);
        probeOutput.setText("");
        for(String name:playerList){
            probeOutput.setText(probeOutput.getText() + name + "; ");
        }
    }

    public void openGameLoopPage(View v){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }
}