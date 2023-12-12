package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.databinding.ActivityStatsBinding;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcardmobile.ui.view.StatsTabsAdapter;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.flashcardmobile.databinding.ActivityStatsBinding binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        
        StatsTabsAdapter sectionsPagerAdapter = new StatsTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        
    }
}