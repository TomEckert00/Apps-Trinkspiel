package com.example.trinkspiel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private static AbstractGamePackage selectedPackage;
    private Button button_standardPacket;
    private Button button_onlinePacket;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);
        button_standardPacket = findViewById(R.id.button_standardPacket);
        button_onlinePacket =findViewById(R.id.button_onlinePacket);
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
            startGameButton.setEnabled(false);
            button_onlinePacket.setEnabled(true);
            button_standardPacket.setEnabled(true);
    }

    public void startGameWithSelectedPackage(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    public void selectStandardPackage(View view){
        selectedPackage = new GamePackageStandard();
        startGameButton.setEnabled(true);
        button_standardPacket.setEnabled(false);
        button_onlinePacket.setEnabled(true);

    }

    public void selectOnlinePackage(View view){
        selectedPackage = new GamePackageOnline();
        startGameButton.setEnabled(true);
        button_onlinePacket.setEnabled(false);
        button_standardPacket.setEnabled(true);
    }


    public void redirectBack(View v){
        this.finish();
        return;
    }

    public static AbstractGamePackage getSelectedPackage(){
        return selectedPackage;
    }
}