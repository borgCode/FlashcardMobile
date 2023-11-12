package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.ui.dialog.DeleteConfirmationDialog;
import com.example.flashcardmobile.ui.view.DeckAdapter;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DeckSelectionFragment extends Fragment implements DeckAdapter.OnDeckOperationListener,
        DeleteConfirmationDialog.DeleteDialogListener {
    
    

    private DeckViewModel deckViewModel;
    private SharedPracticeViewModel sharedPracticeViewModel;
    private RecyclerView recyclerView;
    private DeckAdapter deckAdapter;
    private List<Deck> decks;
    private long deckToDelete;

    @Override
    public void onDeleteDeck(long deckId) {
        deckToDelete = deckId;
        showDeleteDialog();
    }

    @Override
    public void onPracticeDeck(long deckId) {
        sharedPracticeViewModel.setDeckId(deckId);
    }

    private void showDeleteDialog() {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
        dialog.setDeleteDialogListener(this);
        dialog.show(getParentFragmentManager(), "deleteDialog");
    }
    
    @Override
    public void onConfirmDelete() {
        deckViewModel.deleteByDeckId(deckToDelete);
    }
    
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
        
        decks = new ArrayList<>();
        
        sharedPracticeViewModel = new ViewModelProvider(requireActivity()).get(SharedPracticeViewModel.class);

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), new Observer<List<Deck>>() {
            @Override
            public void onChanged(List<Deck> newDecks) {
                deckAdapter.setDecks(newDecks);
            }
        });

        recyclerView = view.findViewById(R.id.deckSelectionView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckAdapter = new DeckAdapter(this, getParentFragmentManager(), decks);
        recyclerView.setAdapter(deckAdapter);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Decks");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return view;
    }
}
