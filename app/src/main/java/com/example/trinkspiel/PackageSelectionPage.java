package com.example.trinkspiel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class PackageSelectionPage extends AppCompatActivity {

    private ArrayList<String> playerList;
    private static String selectedPackageName;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_selection_page);
        startGameButton = findViewById(R.id.button_startGameLoop);
        TextView probeOutput = findViewById(R.id.playerListOutput);
        showPlayerList(probeOutput);
    }

    private void showPlayerList(TextView probeOutput) {
        playerList = GroupSelectionPage.getPlayerList();
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
    }

    public void startGameWithSelectedPackage(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    public void selectStandardPackage(View view){
        selectedPackageName = "StandardPackage";
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(0);
    }

    public void selectOnlinePackage(View view){
        selectedPackageName = "OnlinePackage";
        startGameButton.setEnabled(true);
        resetAllColorsFromPackages();
        highLightSelectedPackage(1);
    }


    private void highLightSelectedPackage(int index) {
        LinearLayout cardViews = findViewById(R.id.package_cards);
        CardView selectedCardView = (CardView) cardViews.getChildAt(index);
        selectedCardView.setCardBackgroundColor(getResources().getColor(R.color.flo3));
    }

    private void resetAllColorsFromPackages() {
        LinearLayout cardViews = findViewById(R.id.package_cards);
        for(int i=0;i<cardViews.getChildCount();i++){
            ((CardView) cardViews.getChildAt(i)).setCardBackgroundColor(getResources().getColor(R.color.flo1));
        }
    }


    public void redirectBack(View v){
        this.finish();
        return;
    }

    public static String getSelectedPackage(){
        return selectedPackageName;
    }
}