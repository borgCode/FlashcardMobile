package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.ui.view.DeckViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewDeckFragment extends Fragment {
    
    private SharedViewModel sharedViewModel;
    private CardViewModel cardViewModel;
    private DeckViewModel deckViewModel;
    private RecyclerView recyclerView;
    private DeckViewAdapter deckViewAdapter;
    private List<Card> cards;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        
        cards = new ArrayList<>();

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckViewAdapter = new DeckViewAdapter(cards);
        recyclerView.setAdapter(deckViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        sharedViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getAllDeckCards(deckId).observe(getViewLifecycleOwner(), newCards -> {
                cards.clear();
                cards.addAll(newCards);
                deckViewAdapter.notifyDataSetChanged();
            });
            deckViewModel.getDeckById(deckId).observe(getViewLifecycleOwner(), deck -> {
                if (actionBar != null) {
                    actionBar.setTitle(deck.getDeckName());
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            });

        });
        return view; 
    }
}
