package com.example.trinkspiel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

    public void openInstagramOfFlo(View view){
        Uri uri = Uri.parse("https://www.instagram.com/_floeck__/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openFlaticon(View view){
        Uri uri = Uri.parse("https://www.flaticon.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openFreepik(View view){
        Uri uri = Uri.parse("https://www.freepik.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openPixelPerfekt(View view){
        Uri uri = Uri.parse("https://www.flaticon.com/authors/pixel-perfect");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}