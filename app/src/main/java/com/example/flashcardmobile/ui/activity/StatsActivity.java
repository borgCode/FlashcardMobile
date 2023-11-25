package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcardmobile.ui.view.StatsTabsAdapter;
import com.example.flashcardmobile.databinding.ActivityPlaceholderStatsBinding;

public class StatsActivity extends AppCompatActivity {

    private ActivityPlaceholderStatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlaceholderStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StatsTabsAdapter sectionsPagerAdapter = new StatsTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        
    }
}