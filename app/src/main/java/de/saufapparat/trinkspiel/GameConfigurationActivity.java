package de.saufapparat.trinkspiel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import de.saufapparat.trinkspiel.util.GamePackageManager;
import lombok.Setter;

public class GameConfigurationActivity extends AppCompatActivity {

    Spinner dropdown_trink;
    Spinner dropdown_art;
    Spinner dropdown_random;
    private String selectedMultiplier = "1";

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
        String selected = selectedMultiplier.replace("x", "");
        GamePackageManager.setMultiplier(Double.parseDouble(selected));
        startActivity(intent);
    }

    private void initializeViews() {
        dropdown_trink = findViewById(R.id.spinner_trink);
        dropdown_art = findViewById(R.id.spinner_art);
        dropdown_random = findViewById(R.id.spinner_random);

        String[] items_trink = new String[]{"0.5x","1x","2x","3x","5x"};
        ArrayAdapter<String> adapter_trink = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_trink);
        dropdown_trink.setAdapter(adapter_trink);
        dropdown_trink.setSelection(1);
        dropdown_trink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMultiplier = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMultiplier = "1";
            }

        });

        String[] items_art = new String[]{"normal","shots"};
        ArrayAdapter<String> adapter_art = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_art);
        dropdown_art.setAdapter(adapter_art);
        dropdown_art.setSelection(0);

        String[] items_random = new String[]{"aus","selten","häufig"};
        ArrayAdapter<String> adapter_random = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_random);
        dropdown_random.setAdapter(adapter_random);
        dropdown_random.setSelection(0);
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
}