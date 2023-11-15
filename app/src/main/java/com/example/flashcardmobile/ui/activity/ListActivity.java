package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.ui.view.ListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    
    private DeckCardViewModel deckCardViewModel;
    private CardViewModel cardViewModel;
    private RecyclerView recyclerView;
    private ListViewAdapter listViewAdapter;
    private List<DeckCard> cards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        cards = new ArrayList<>();

        deckCardViewModel = new ViewModelProvider(this).get(DeckCardViewModel.class);
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listViewAdapter = new ListViewAdapter(cards);
        recyclerView.setAdapter(listViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        
        deckCardViewModel.getAllCards().observe(this, newCards -> {
            cards.clear();
            cards.addAll(newCards);
            listViewAdapter.notifyDataSetChanged();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        
        
        
        
        
        
    }
}
