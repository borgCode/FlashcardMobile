package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeckListViewFragment extends Fragment implements DeckListViewAdapter.OnDeckOperationListener {

    private DeckViewModel deckViewModel;
    private CardViewModel cardViewModel;
    private RecyclerView recyclerView;
    private DeckListViewAdapter deckListViewAdapter;
    private TextView firstColumn;
    private TextView secondColumn;
    private TextView thirdColumn;
    private Button backToDecksBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck_list_view, container, false);
        
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        deckListViewAdapter = new DeckListViewAdapter(this);
        recyclerView.setAdapter(deckListViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        
        deckViewModel.getAllDecksWithInfo().observe(getViewLifecycleOwner(), decks -> {
            deckListViewAdapter.setDecks(new ArrayList<>(decks));
            deckListViewAdapter.notifyDataSetChanged();
        });


        firstColumn = view.findViewById(R.id.firstCol);
        secondColumn = view.findViewById(R.id.secondCol);
        thirdColumn = view.findViewById(R.id.thirdCol);
        backToDecksBtn = view.findViewById(R.id.back_to_decks_button);
        
        backToDecksBtn.setOnClickListener(v -> {
            thirdColumn.setVisibility(View.VISIBLE);
            backToDecksBtn.setVisibility(View.GONE);
            setColumnWeights(1.2F);
            setColumnNames("Deck name", "Deck size");
            deckListViewAdapter.setShowingDecks();
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
        thirdColumn.setVisibility(View.GONE);
        backToDecksBtn.setVisibility(View.VISIBLE);
        
        setColumnWeights(1.8F);
        setColumnNames("Front side", "Back side");
        
        cardViewModel.getAllDeckCards(deckId).observe(getViewLifecycleOwner(), cards -> {
            deckListViewAdapter.setCards(new ArrayList<>(cards));
            deckListViewAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onViewDueCards(long deckId) {
        thirdColumn.setVisibility(View.GONE);
        backToDecksBtn.setVisibility(View.VISIBLE);

        setColumnWeights(1.8F);
        setColumnNames("Front side", "Back side");

        cardViewModel.getDueCards(deckId).observe(getViewLifecycleOwner(), cards -> {
            deckListViewAdapter.setDueCards(new ArrayList<>(cards));
            deckListViewAdapter.notifyDataSetChanged();
        });

    }

    private void setColumnWeights(Float weight) {
        LinearLayout.LayoutParams firstColParams = (LinearLayout.LayoutParams) firstColumn.getLayoutParams();
        firstColParams.weight = weight;

        LinearLayout.LayoutParams secondColParams = (LinearLayout.LayoutParams) secondColumn.getLayoutParams();
        secondColParams.weight = weight;

        firstColumn.setLayoutParams(firstColParams);
        secondColumn.setLayoutParams(secondColParams);
    }
    private void setColumnNames(String columnOne, String columnTwo) {
        firstColumn.setText(columnOne);
        secondColumn.setText(columnTwo);
    }
}
