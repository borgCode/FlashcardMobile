package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.view.CardListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;
import com.example.flashcardmobile.viewmodel.SharedDeckAndCardViewModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CardListViewFragment extends Fragment implements CardListViewAdapter.onCardOperationListener {

    private DeckCardViewModel deckCardViewModel;
    private CardViewModel cardViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private CardListViewAdapter cardListViewAdapter;
    private String selectedOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list_view, container, false);
        
        deckCardViewModel = new ViewModelProvider(requireActivity()).get(DeckCardViewModel.class);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardListViewAdapter = new CardListViewAdapter(this);
        recyclerView.setAdapter(cardListViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));

        deckCardViewModel.getAllCards().observe(getViewLifecycleOwner(), newCards -> {
            cardListViewAdapter.setCards(new ArrayList<>(newCards));
            cardListViewAdapter.setCardsFull(new ArrayList<>(newCards));
            cardListViewAdapter.notifyDataSetChanged();
        });
        
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.search_view_with_spinner) {

                    View actionView = menuItem.getActionView();
                    Spinner spinner = actionView.findViewById(R.id.spinner_search_options);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            getActivity(), R.array.search_options, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedOption = parent.getItemAtPosition(position).toString().toLowerCase().trim();
                            
                                cardListViewAdapter.setCurrentSearchColumn(selectedOption);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    SearchView searchView = actionView.findViewById(R.id.search_view);
                    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            if (selectedOption.equals("tag")) {
                                
                                observeCardsFilteredByTag(newText);
                            } else {
                                cardListViewAdapter.getFilter().filter(newText);
                            }
                            return false;
                        }
                    });
                }
                return true;
            }
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

    public void observeCardsFilteredByTag(String tag) {
        deckCardViewModel.getCardsByTag(tag).observe(getViewLifecycleOwner(), newCards -> {
            cardListViewAdapter.setCards(new ArrayList<>(newCards));
            cardListViewAdapter.notifyDataSetChanged();
        });
    }
}
