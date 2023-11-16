package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.ui.view.ListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;
import org.jetbrains.annotations.NotNull;

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
        listViewAdapter = new ListViewAdapter();
        recyclerView.setAdapter(listViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        
        deckCardViewModel.getAllCards().observe(this, newCards -> {
            listViewAdapter.setCards(new ArrayList<>(newCards));
            listViewAdapter.setCardsFull(new ArrayList<>(newCards));
//            cards.clear();
//            cards.addAll(newCards);
            listViewAdapter.notifyDataSetChanged();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
                
                int id = menuItem.getItemId();
                if (id == R.id.search_list) {
                    SearchView searchView = (SearchView) menuItem.getActionView();
                    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            listViewAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
                return true;
            }
        });
        
    }
}
