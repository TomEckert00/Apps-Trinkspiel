package com.example.trinkspiel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openGroupPage(View v){
        Intent intent = new Intent(this, GroupPage.class);
        startActivity(intent);
    }

    public void openInstagram(View v){
        Uri uri = Uri.parse("https://www.instagram.com/tom_eckert_/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openMoreInformationPage(View v){
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }

    public void openMainSettings(View v){
        Intent intent = new Intent(this, MainSettings.class);
        startActivity(intent);
    }
}