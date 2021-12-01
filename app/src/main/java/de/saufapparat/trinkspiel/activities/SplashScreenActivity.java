package de.saufapparat.trinkspiel.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import de.saufapparat.trinkspiel.R;
import de.saufapparat.trinkspiel.activities.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final long SPLASHSCREEN_TIME_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startNewIntent();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable,SPLASHSCREEN_TIME_MILLIS);

    }

    private void startNewIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}