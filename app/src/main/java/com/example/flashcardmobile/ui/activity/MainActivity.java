package com.example.flashcardmobile.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.flashcardmobile.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcardmobile.util.BadgeManager;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        
        String theme = sharedPreferences.getString("Theme", "Light");
        if (theme.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        BadgeManager badgeManager = BadgeManager.getInstance(getApplication());
        badgeManager.initializeBadges();
        
        setContentView(R.layout.activity_main);


        Button practiceButton = findViewById(R.id.practiceBtn);
        Button listButton = findViewById(R.id.listBtn);
        Button settingsButton = findViewById(R.id.settingsBtn);
        Button statsButton = findViewById(R.id.statsBtn);

        practiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
            startActivity(intent);
        });
        
        listButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
        });

    }
}