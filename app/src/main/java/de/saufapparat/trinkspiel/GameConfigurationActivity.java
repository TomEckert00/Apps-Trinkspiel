package de.saufapparat.trinkspiel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.saufapparat.trinkspiel.enmus.ActivitySpezialEnum;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.enmus.HotSpezialEnum;
import de.saufapparat.trinkspiel.enmus.OnlineSpezialEnum;
import de.saufapparat.trinkspiel.enmus.Trinkstaerke;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import lombok.Getter;
import lombok.Setter;

public class GameConfigurationActivity extends AppCompatActivity {

    Spinner dropdown_trinkstaerke;
    Spinner dropdown_getraenkeTyp;
    Spinner dropdown_spezial;

    private Trinkstaerke selectedTrinkstaerke = Trinkstaerke.normal;
    private GetraenkeTyp selectedGetraenkeTyp = GetraenkeTyp.schlucke;
    @Getter
    private static String selectedSpezialPlayer;
    private static String selectedSpezial;

    @Setter
    private static boolean configsSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_configuration);
        initializeViews();
    }

    public void startGameWithSelectedPackage(View view) {
        Intent intent = new Intent(this, GameLoop.class);
        GamePackageManager.setTrinkstaerke(selectedTrinkstaerke);
        GamePackageManager.setGetraenkeTyp(selectedGetraenkeTyp);
        GameLoop.setGetraenkeTyp(selectedGetraenkeTyp);
        startActivity(intent);
    }

    private void initializeViews() {
        dropdown_trinkstaerke = findViewById(R.id.spinner_trink);
        dropdown_getraenkeTyp = findViewById(R.id.spinner_art);
        dropdown_spezial = findViewById(R.id.spinner_spezial);


        configureSpinner(dropdown_trinkstaerke, Arrays.asList(Trinkstaerke.values()), 1, new TrinkstaerkeListener());
        configureSpinner(dropdown_getraenkeTyp, Arrays.asList(GetraenkeTyp.values()), 0 , new GetraenkeTypListener());
        configureSpinner(dropdown_spezial, decideSpezialItems(), 0 , new SpezialConfigListener());
    }

    private void configureSpinner(Spinner spinner, List items, int selection, AdapterView.OnItemSelectedListener listener){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(selection);
        spinner.setOnItemSelectedListener(listener);
    }

    private List decideSpezialItems() {
        GamePackage selectedPackage = PackageSelectionPage.getSelectedPackage();
        switch (selectedPackage){
            case StandardPackage:
                //todo: Auslagern in strings
                ArrayList<String> list =  new ArrayList<>(Arrays.asList("niemand1234321"));
                list.addAll(GroupSelectionPage.getPlayerList());
                return list;
            case OnlinePackage:
                return Arrays.asList(OnlineSpezialEnum.values());
            case ActivityPackage:
                return Arrays.asList(ActivitySpezialEnum.values());
            case HotPackage:
                return Arrays.asList(HotSpezialEnum.values());
            default:
                List def = new ArrayList();
                def.add("empty");
                return def;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (configsSet){
            finish();
            return;
        }
    }

    public void backToPackageSelectionPage(View view){
        finish();
        return;
    }

    private class TrinkstaerkeListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedTrinkstaerke = Trinkstaerke.valueOf(parent.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selectedTrinkstaerke = Trinkstaerke.normal;
        }
    }

    private class GetraenkeTypListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedGetraenkeTyp = GetraenkeTyp.valueOf(parent.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selectedGetraenkeTyp = GetraenkeTyp.schlucke;
        }
    }

    private class SpezialConfigListener implements AdapterView.OnItemSelectedListener{

        //todo: refactoring mit übergabe ergebnis
        /*
        String ergebnis = "";
        public SpezialConfigListener(String ergebnis){
            this.ergebnis = ergebnis
        }
         */

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedSpezialPlayer = parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selectedSpezialPlayer = "niemand1234321";
        }
    }
}