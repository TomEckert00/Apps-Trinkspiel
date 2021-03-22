package com.example.trinkspiel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PackageSelectionPage extends AppCompatActivity {

    private ArrayList<String> playerList;
    private static String selectedPackageName;
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
        int i = 0;
        for(String name:playerList){
            i++;
            if(i<playerList.size()){
                probeOutput.setText(probeOutput.getText() + name + ", ");
            }else{
                probeOutput.setText(probeOutput.getText() + name);
            }
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
        selectedPackageName = "StandardPackage";
        startGameButton.setEnabled(true);
        button_standardPacket.setEnabled(false);
        button_onlinePacket.setEnabled(true);

    }

    public void selectOnlinePackage(View view){
        selectedPackageName = "OnlinePackage";
        startGameButton.setEnabled(true);
        button_onlinePacket.setEnabled(false);
        button_standardPacket.setEnabled(true);
    }


    public void redirectBack(View v){
        this.finish();
        return;
    }

    public static String getSelectedPackage(){
        return selectedPackageName;
    }
}