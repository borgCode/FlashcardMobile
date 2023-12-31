package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;

import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.databinding.ActivityListBinding;
import com.example.flashcardmobile.ui.view.ListTabsAdapter;
import com.google.android.material.tabs.TabLayout;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.flashcardmobile.databinding.ActivityListBinding binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        ListTabsAdapter sectionsPagerAdapter = new ListTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}
