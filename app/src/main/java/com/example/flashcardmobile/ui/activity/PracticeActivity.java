package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.fragment.DeckSelectionFragment;
import com.example.flashcardmobile.ui.fragment.PracticeFragment;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;

public class PracticeActivity extends AppCompatActivity {
    private SharedPracticeViewModel sharedPracticeViewModel;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        sharedPracticeViewModel = new ViewModelProvider(this).get(SharedPracticeViewModel.class);
        sharedPracticeViewModel.getId().observe(this, id -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PracticeFragment())
                    .commit();
        
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DeckSelectionFragment())
                    .commit();
        }
    }
    

}
