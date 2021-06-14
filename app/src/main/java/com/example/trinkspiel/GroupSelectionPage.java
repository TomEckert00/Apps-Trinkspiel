package com.example.trinkspiel;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GroupSelectionPage extends AppCompatActivity {

    private static ArrayList<String> playerList;
    private LinearLayout playerListLayout;
    private TextView.OnEditorActionListener editorActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection_page);

        initializeViews();
    }

    private void initializeViews() {
        playerListLayout = findViewById(R.id.playerListLinearLayout);
        editorActionListener = new CustomActionListener();
        ((EditText) playerListLayout.getChildAt(playerListLayout.getChildCount()-2)).setOnEditorActionListener(editorActionListener);
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
        int i=1;
        while(playerList.size() < 2){
            playerList.add("Neuer Spieler " + i);
            i++;
        }
    }

    private void openNextIntent() {
        Intent intent = new Intent(this, PackageSelectionPage.class);
        startActivity(intent);
    }

    public void addNewPlayerInput(View v){
        int newFieldIndex = playerListLayout.getChildCount()-1;
        int lastFieldIndex = playerListLayout.getChildCount()-2;

        ((EditText) playerListLayout.getChildAt(lastFieldIndex)).setOnEditorActionListener(null);

        EditText newField = prepareNewField(newFieldIndex);
        playerListLayout.addView(newField,newFieldIndex);
    }

    private EditText prepareNewField(int newFieldIndex) {
        EditText newField = new EditText(this);
        newField.setSingleLine();
        newField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        newField.setOnEditorActionListener(editorActionListener);
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

     private class CustomActionListener implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_NEXT:
                    addNewPlayerInput(null);
                    break;
                default:
                    break;
            }
            return false;
        }
    };
}