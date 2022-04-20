package de.saufapparat.trinkspiel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.enmus.ActivitySpezialEnum;
import de.saufapparat.trinkspiel.enmus.GamePackage;
import de.saufapparat.trinkspiel.enmus.GetraenkeTyp;
import de.saufapparat.trinkspiel.enmus.HotSpezialEnum;
import de.saufapparat.trinkspiel.enmus.Trinkstaerke;
import de.saufapparat.trinkspiel.util.GamePackageManager;
import de.saufapparat.trinkspiel.util.HelperUtil;
import de.saufapparat.trinkspiel.util.TinyDB;
import lombok.Getter;
import lombok.Setter;

public class GameConfigurationActivity extends AppCompatActivity {

    Spinner dropdown_trinkstaerke;
    Spinner dropdown_getraenkeTyp;
    Spinner dropdown_spezial;

    TextView spezial_textview;

    private Trinkstaerke selectedTrinkstaerke = Trinkstaerke.normal;
    private GetraenkeTyp selectedGetraenkeTyp = GetraenkeTyp.schlucke;
    @Getter
    private static String selectedSpezialPlayer;
    @Getter
    private static String selectedSpezialActivity;
    @Getter
    private static HotSpezialEnum selectedSpezialHot;

    @Setter
    private static boolean configsSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_configuration);
        initializeViews();
        selectedSpezialPlayer=null;
        selectedSpezialActivity=null;
        if("true".equals(getIntent().getStringExtra("quickplay"))){
            TinyDB tinyDB = new TinyDB(getApplicationContext());
            Trinkstaerke trinkstaerke = tinyDB.getObject("trinkstaerke", Trinkstaerke.class);
            GetraenkeTyp getraenkeTyp = tinyDB.getObject("getraenketyp", GetraenkeTyp.class);

            Intent intent = new Intent(this, GameLoop.class);
            intent.putExtra("quickplay", "true");
            GamePackageManager.setTrinkstaerke(trinkstaerke);
            GamePackageManager.setGetraenkeTyp(getraenkeTyp);
            GameLoop.setGetraenkeTyp(getraenkeTyp);
            startActivity(intent);
        }
    }

    public void startGameWithSelectedPackage(View view) {
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        tinyDB.putObject("trinkstaerke", selectedTrinkstaerke);
        tinyDB.putObject("getraenketyp", selectedGetraenkeTyp);

        Intent intent = new Intent(this, GameLoop.class);
        GamePackageManager.setTrinkstaerke(selectedTrinkstaerke);
        GamePackageManager.setGetraenkeTyp(selectedGetraenkeTyp);
        GameLoop.setGetraenkeTyp(selectedGetraenkeTyp);
        startActivity(intent);
    }

    private void initializeViews() {
        spezial_textview = findViewById(R.id.textView_spezial);
        dropdown_trinkstaerke = findViewById(R.id.spinner_trink);
        dropdown_getraenkeTyp = findViewById(R.id.spinner_art);
        dropdown_spezial = findViewById(R.id.spinner_spezial);

        configureSpinner(dropdown_trinkstaerke, Trinkstaerke.getValuesWithLanguage(getApplicationContext()), 1, new TrinkstaerkeListener());
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
        switch (selectedPackage) {
            case StandardPackage:
                spezial_textview.setText(getString(R.string.spezial_textview_standard));
                return getListWithPlayersAndStandardOptions();
            case OnlinePackage:
                spezial_textview.setText(getString(R.string.spezial_textview_online));
                return getListWithPlayersAndStandardOptions();
            case ActivityPackage:
                spezial_textview.setText(getString(R.string.spezial_textview_activity));
                return Arrays.asList(ActivitySpezialEnum.values());
            case HotPackage:
                spezial_textview.setText(getString(R.string.spezial_textview_hot));
                return Arrays.asList(HotSpezialEnum.values());
        }
        return new ArrayList();
    }

    private ArrayList<String> getListWithPlayersAndStandardOptions() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                getString(R.string.spezial_standardpack_aus),
                getString(R.string.spezial_standardpack_zufall)));
        list.addAll(GroupSelectionPage.getPlayerList());
        return list;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (configsSet){
            finish();
            return;
        }
        fetchRandomPlayerForSpezialConfiguration();
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
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (PackageSelectionPage.getSelectedPackage()){
                case StandardPackage:
                case OnlinePackage:
                    selectedSpezialPlayer = parent.getSelectedItem().toString();
                    fetchRandomPlayerForSpezialConfiguration();
                    break;
                case ActivityPackage:
                    selectedSpezialActivity = parent.getSelectedItem().toString();
                    break;
                case HotPackage:
                    selectedSpezialHot = HotSpezialEnum.valueOf(parent.getSelectedItem().toString());
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            switch (PackageSelectionPage.getSelectedPackage()) {
                case StandardPackage:
                case OnlinePackage:
                    selectedSpezialPlayer = getString(R.string.spezial_standardpack_aus);
                    break;
                case ActivityPackage:
                    selectedSpezialActivity = getString(R.string.spezial_standardpack_aus);
                    break;
                case HotPackage:
                    selectedSpezialHot = HotSpezialEnum.sicher;
                    break;
            }
        }
    }

    private void fetchRandomPlayerForSpezialConfiguration() {
        if(selectedSpezialPlayer!=null){
            Spinner spinner = (Spinner) findViewById(R.id.spinner_spezial);
            if (spinner.getSelectedItemPosition() == 1){
                int randomNumber = HelperUtil.getRandomNumber(0, GroupSelectionPage.getPlayerList().size());
                selectedSpezialPlayer = GroupSelectionPage.getPlayerList().get(randomNumber);
            }
        }
    }
}