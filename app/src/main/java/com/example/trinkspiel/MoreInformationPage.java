package com.example.trinkspiel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trinkspiel.util.Constants;

public class MoreInformationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information_page);
        TextView version =(TextView) findViewById(R.id.software_version);
        version.setText("Version: " + Constants.SOFTWARE_VERSION);
    }
}