package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.ui.dialog.DeleteConfirmationDialog;
import com.example.flashcardmobile.ui.view.DeckAdapter;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedDeckAndCardViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeckSelectionFragment extends Fragment implements DeckAdapter.OnDeckOperationListener,
        DeleteConfirmationDialog.DeleteDialogListener {


    private DeckViewModel deckViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private DeckAdapter deckAdapter;
    private long deckToDelete;
    
    

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck_selection, viewGroup, false);

        List<Deck> decks = new ArrayList<>();

        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), new Observer<List<Deck>>() {
            @Override
            public void onChanged(List<Deck> newDecks) {
                deckAdapter.setDecks(newDecks);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.deckSelectionView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckAdapter = new DeckAdapter(this, decks);
        recyclerView.setAdapter(deckAdapter);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Decks");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                
                menuInflater.inflate(R.menu.menu_deck_selection_dropdown, menu);
                
            }

            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.AddDeckItem) {
                    

                    AddDeckFragment addDeckFragment = new AddDeckFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, addDeckFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                } else if (id == R.id.deleteAllDecksItem) {
                    showDeleteDialog("Do you really want to delete all decks?", "all_decks");
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        
        return view;
    }

    @Override
    public void onDeleteDeck(long deckId) {
        deckToDelete = deckId;
        showDeleteDialog("Do you really want to delete this deck?", "single_deck");
    }

    @Override
    public void onPracticeDeck(long deckId) {
        sharedDeckAndCardViewModel.setDeckId(deckId);
        
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new PracticeFragment())
                .addToBackStack(null)
                .commit();

    }

    public void onViewDeck(long deckId) {
        sharedDeckAndCardViewModel.setDeckId(deckId);
        
        ViewDeckFragment viewDeckFragment = new ViewDeckFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, viewDeckFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onAddCard(long deckId) {
        sharedDeckAndCardViewModel.setDeckId(deckId);

        AddCardFragment addCardFragment = new AddCardFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addCardFragment)
                .addToBackStack(null)
                .commit();

    }

    private void showDeleteDialog(String dialogMessage, String confirmationType) {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(dialogMessage, confirmationType);
        dialog.setDeleteDialogListener(this);
        dialog.show(getParentFragmentManager(), "deleteDialog");
    }

    @Override
    public void onConfirmDelete(String confirmationType) {
        if (confirmationType.equals("single_deck")) {
            deckViewModel.deleteByDeckId(deckToDelete);
        } else if (confirmationType.equals("all_decks")) {
            deckViewModel.deleteAllDecks();
        }
    }
}
