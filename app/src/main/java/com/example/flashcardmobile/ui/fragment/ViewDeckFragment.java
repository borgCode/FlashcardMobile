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
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewDeckFragment extends Fragment {
    
    private SharedPracticeViewModel sharedPracticeViewModel;
    private CardViewModel cardViewModel;
    private RecyclerView recyclerView;
    private DeckViewAdapter deckViewAdapter;
    private List<Card> cards;

    public ViewDeckFragment() {
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        
        cards = new ArrayList<>();

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedPracticeViewModel = new ViewModelProvider(requireActivity()).get(SharedPracticeViewModel.class);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckViewAdapter = new DeckViewAdapter(cards);
        recyclerView.setAdapter(deckViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));

        sharedPracticeViewModel.getId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getAllCards(deckId).observe(getViewLifecycleOwner(), newCards -> {
                cards.clear();
                cards.addAll(newCards);
                deckViewAdapter.notifyDataSetChanged();
            });

        });

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        sharedPracticeViewModel.getName().observe(getViewLifecycleOwner(), deckName -> {
            if (actionBar != null) {
                actionBar.setTitle(deckName);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        });
        
        
        
        return view; 
    }
}
