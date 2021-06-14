package com.example.trinkspiel;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GroupSelectionPage extends AppCompatActivity {

    private static ArrayList<String> playerList;
    private LinearLayout playerListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection_page);

        initializeViews();
    }

    private void initializeViews() {
        playerListLayout = findViewById(R.id.playerListLinearLayout);
    }

    public void openPackageSelectionPage(View v){
        preparePlayerList();
        openNextIntent();
    }

    private void preparePlayerList() {
        playerList = new ArrayList<>();
        fillListWithPlayerNames();
        validatePlayerList();
    }

    private void fillListWithPlayerNames() {
        for (int index=0; index<playerListLayout.getChildCount()-1; index++){
            String name = ((EditText)playerListLayout.getChildAt(index)).getText().toString();
            if(!TextUtils.isEmpty(name)){
                playerList.add(name);
            }
        }
    }

    private void validatePlayerList() {
        if(playerList.size()==0){
            playerList.add(getString(R.string.player1_hint));
            playerList.add(getString(R.string.player2_hint));
        }
        else if(playerList.size()==1){
            if (playerList.get(0).equals(getString(R.string.player2_hint))){
                playerList.add(getString(R.string.player1_hint));
            }else{
                playerList.add(getString(R.string.player2_hint));
            }
        }
    }

    private void openNextIntent() {
        Intent intent = new Intent(this, PackageSelectionPage.class);
        startActivity(intent);
    }

    public void addNewPlayerInput(View v){
        int newFieldIndex = playerListLayout.getChildCount()-1;

        EditText newField = prepareNewField(newFieldIndex);
        playerListLayout.addView(newField,newFieldIndex);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(newField, InputMethodManager.SHOW_IMPLICIT);
    }

    private EditText prepareNewField(int newFieldIndex) {
        EditText newField = new EditText(this);
        newField.requestFocus();
        newField.setSingleLine();
        newField.setHintTextColor(getResources().getColor(R.color.white));
        newField.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        newField.setTextColor(getResources().getColor(R.color.white));
        newField.setHint(getString(R.string.player_hint) + " " + (newFieldIndex+1));
        return newField;
    }

    public static ArrayList<String> getPlayerList(){
        return playerList;
    }

    public void redirectBack(View v){
        this.finish();
        return;
    }

}