package de.saufapparat.trinkspiel.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.TinyDB;

public class GroupSelectionPage extends AppCompatActivity {

    private static ArrayList<String> playerList;
    private LinearLayout playerListLayout;
    private TextView.OnEditorActionListener editorActionListener;
    private EditText player1EditText;
    private ImageView closeKeyBoardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection_page);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HelperUtil.removeNavigationBarBottom(this);
    }

    private void initializeViews() {
        playerListLayout = findViewById(R.id.playerListLinearLayout);
        player1EditText = findViewById(R.id.player1EditText);
        closeKeyBoardImage = findViewById(R.id.closeKeyBoardImage);
        editorActionListener = new CustomActionListener();
        player1EditText.setOnEditorActionListener(editorActionListener);
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
            LinearLayout lay = (LinearLayout) playerListLayout.getChildAt(index);
            String name = ((EditText) lay.getChildAt(0)).getText().toString();
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
        savePlayersToPreferences();
        Intent intent = new Intent(this, PackageSelectionPage.class);
        startActivity(intent);
    }

    private void savePlayersToPreferences() {
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        tinyDB.putListString("spielerListe", playerList);
    }

    public void addNewPlayerInput(View v){
        int newFieldIndex = playerListLayout.getChildCount()-1;
        int oldField = playerListLayout.getChildCount()-2;

        LinearLayout lay = (LinearLayout) playerListLayout.getChildAt(oldField);
        lay.removeViewAt(1);
        ((EditText)lay.getChildAt(0)).setOnEditorActionListener(null);

        EditText newField = prepareNewField(newFieldIndex);
        playerListLayout.addView(constructLinearLayout(newFieldIndex),newFieldIndex);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(newField, InputMethodManager.SHOW_IMPLICIT);
    }

    private View constructLinearLayout(int playerNumber) {
        LinearLayout linear = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setWeightSum(10.0f);
        linear.setLayoutParams(params);
        linear.addView(prepareNewField(playerNumber),0);
        linear.addView(closeKeyBoardImage,1);
        return linear;
    }

    private EditText prepareNewField(int playerNumber) {
        EditText newField = new EditText(this);
        newField.setSingleLine();
        newField.requestFocus();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            0,LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.weight=9;
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        newField.setLayoutParams(params);

        newField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        newField.setOnEditorActionListener(editorActionListener);
        newField.setHintTextColor(getResources().getColor(R.color.white));
        newField.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        newField.setTextColor(getResources().getColor(R.color.white));
        newField.setHint(getString(R.string.player_hint) + " " + (playerNumber+1));
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
    }

    public void closeKeyBoard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null){
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}