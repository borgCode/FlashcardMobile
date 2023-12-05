package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.dialog.EditDeckNameDialog;
import com.example.flashcardmobile.ui.view.DeckListViewAdapter;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeckListViewFragment extends Fragment implements DeckListViewAdapter.onDeckOperationListener {

    private DeckViewModel deckViewModel;
    private RecyclerView recyclerView;
    private DeckListViewAdapter deckListViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck_list_view, container, false);
        
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckListViewAdapter = new DeckListViewAdapter(this);
        recyclerView.setAdapter(deckListViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        
        deckViewModel.getAllDecksWithInfo().observe(getViewLifecycleOwner(), decks -> {
            deckListViewAdapter.setDecks(new ArrayList<>(decks));
            deckListViewAdapter.notifyDataSetChanged();
        });
        
        return view;
    }

    @Override
    public void onEditDeckName(long deckId, String deckName) {
        EditDeckNameDialog dialog = EditDeckNameDialog.newInstance(deckId, deckName);
        dialog.show(getActivity().getSupportFragmentManager(), "editName");
    }

    @Override
    public void onViewAllCards(long deckId) {

    }

    @Override
    public void onViewDueCards(long deckId) {

    }
}
