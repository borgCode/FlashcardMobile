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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.ui.view.DeckViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedDeckAndCardViewModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViewDeckFragment extends Fragment implements DeckViewAdapter.onCardOperationListener{
    
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
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
        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckViewAdapter = new DeckViewAdapter(cards, this);
        recyclerView.setAdapter(deckViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),RecyclerView.VERTICAL));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        sharedDeckAndCardViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
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

    @Override
    public void onCardEdit(long deckId, long cardId) {
        cardViewModel.getCardById(cardId).observe(getViewLifecycleOwner(), card -> {
            if (card != null) {
                sharedDeckAndCardViewModel.setSelectedCard(card);
                sharedDeckAndCardViewModel.setDeckId(deckId);
                EditCardFragment editCardFragment = new EditCardFragment();

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, editCardFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public void onResetDueDate(long cardId) {
        cardViewModel.getCardById(cardId).observe(getViewLifecycleOwner(), card -> {
            if (card != null) {
                card.setDueDate(LocalDateTime.now());
                cardViewModel.update(card);
            }
        });

    }

    @Override
    public void onCardDelete(long cardId) {
        cardViewModel.deleteCardById(cardId);
    }
}
