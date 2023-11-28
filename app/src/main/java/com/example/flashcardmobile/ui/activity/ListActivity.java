package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.databinding.ActivityListBinding;
import com.example.flashcardmobile.ui.view.ListTabsAdapter;
import com.google.android.material.tabs.TabLayout;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView backButton = findViewById(R.id.back_button);
        
        backButton.setOnClickListener(view -> onBackPressed());

        ListTabsAdapter sectionsPagerAdapter = new ListTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}
