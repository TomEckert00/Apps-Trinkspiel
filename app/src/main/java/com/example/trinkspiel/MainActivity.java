package com.example.trinkspiel;

import android.content.Intent;
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

    public void openMarketPage(View v){
        Intent intent = new Intent(this, MarketPage.class);
        startActivity(intent);
    }

    public void openMoreInformationPage(View v){
        Intent intent = new Intent(this, MoreInformationPage.class);
        startActivity(intent);
    }
}