package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.ui.view.DeckAdapter;
import com.example.flashcardmobile.viewmodel.AddDeckViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeckSelectionFragment extends Fragment {

    private DeckViewModel deckViewModel;
    private RecyclerView recyclerView;
    private DeckAdapter deckAdapter;
    private List<Deck> decks;
    

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck_selection, viewGroup, false);
        
        FloatingActionButton buttonAddDeck = view.findViewById(R.id.add_deck_button);
        buttonAddDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Add Deck Fragment", "opening add deck fragment");
                
                AddDeckFragment addDeckFragment = new AddDeckFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addDeckFragment)
                        .addToBackStack(null)
                        .commit();
            }
            
        });

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), new Observer<List<Deck>>() {
            @Override
            public void onChanged(List<Deck> decks) {
                Log.d("Deck Selection", "deck select fragment opened");

            }
        });

        recyclerView = view.findViewById(R.id.deckSelectionView);
        deckAdapter = new DeckAdapter(decks);
        recyclerView.setAdapter(deckAdapter);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Decks");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        
        return view;
    }
    
}
