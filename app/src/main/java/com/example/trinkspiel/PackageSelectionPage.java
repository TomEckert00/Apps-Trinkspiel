package com.example.trinkspiel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trinkspiel.gamepackages.AbstractGamePackage;
import com.example.trinkspiel.gamepackages.GamePackageOnline;
import com.example.trinkspiel.gamepackages.GamePackageStandard;

import java.util.ArrayList;

public class PackageSelectionPage extends AppCompatActivity {

    private ArrayList<String> playerList;
    private AbstractGamePackage selectedPackage;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);
        startGameButton = findViewById(R.id.button_startGameLoop);
        playerList = GroupPage.getPlayerList();
        TextView probeOutput = findViewById(R.id.playerListOutput);
        probeOutput.setText("");
        for(String name:playerList){
            probeOutput.setText(probeOutput.getText() + name + "; ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedPackage == null){
            startGameButton.setEnabled(false);
        }
    }

    public void startGameWithSelectedPackage(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    public void selectStandardPackage(View view){
        selectedPackage = new GamePackageStandard();
        startGameButton.setEnabled(true);
    }

    public void selectOnlinePackage(View view){
        selectedPackage = new GamePackageOnline();
        startGameButton.setEnabled(true);
    }


    public void redirectBack(View v){
        this.finish();
        return;
    }
}