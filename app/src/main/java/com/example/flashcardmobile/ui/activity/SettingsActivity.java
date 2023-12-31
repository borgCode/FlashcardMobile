package com.example.flashcardmobile.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import com.example.flashcardmobile.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String theme = sharedPreferences.getString("Theme", "Light");
        if (theme.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        } else {
            
        }

        RadioButton lightModeButton = findViewById(R.id.lightMode);
        RadioButton darkModeButton = findViewById(R.id.darkMode);

        if (theme.equals("Dark")) {
            darkModeButton.setChecked(true);
        } else {
            lightModeButton.setChecked(true);
        }
        
        lightModeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                
                saveThemePreference("Light");
                recreateActivity();            }
        });
        
        darkModeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                
                saveThemePreference("Dark");
                recreateActivity();
            }
        });
        
    }

    private void saveThemePreference(String theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        editor.putString("Theme", theme);
        editor.apply();
    }

    private void recreateActivity() {
        
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    
}
