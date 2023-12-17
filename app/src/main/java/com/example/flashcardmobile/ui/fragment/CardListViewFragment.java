package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.view.CardListViewAdapter;
import com.example.flashcardmobile.util.RecyclerViewSelectionUtil;
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
    private SelectionTracker<Long> selectionTracker;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.selected_items_card_list_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_change_decks) {
                mode.finish();
                return true;
            } else if (id == R.id.action_reset_due_date) {
                mode.finish();
                return true;
            } else if (id == R.id.action_delete) {
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            toggleSelectionMode(false);
            selectionTracker.clearSelection();
            actionMode = null;
        }
    };

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

        Button debugButton = view.findViewById(R.id.debug_button);
        debugButton.setOnClickListener(v -> {
            cardListViewAdapter.notifyDataSetChanged();
        });

        deckCardViewModel.getAllCards().observe(getViewLifecycleOwner(), newCards -> {
            cardListViewAdapter.setCards(new ArrayList<>(newCards));
            cardListViewAdapter.setCardsFull(new ArrayList<>(newCards));
            cardListViewAdapter.notifyDataSetChanged();
        });

        setupSelectionTracker(recyclerView);
        cardListViewAdapter.setSelectionTracker(selectionTracker);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.card_list_view_menu_list, menu);
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
                    return true;
                } else if (id == R.id.select_cards) {
                    toggleSelectionMode(true);
                    return true;
                }
                return false;
            }
        });
        return view;

    }

    private void setupSelectionTracker(RecyclerView recyclerView) {
        selectionTracker = new SelectionTracker.Builder<>(
                "cardSelectionId",
                recyclerView,
                new RecyclerViewSelectionUtil.KeyProvider<>(ItemKeyProvider.SCOPE_MAPPED, position -> cardListViewAdapter.getItemId(position)),
                new RecyclerViewSelectionUtil.DetailsLookup<>(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build();
        Log.d("SelectionTracker", "Tracker initialized: " + (selectionTracker != null));

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(Long key, boolean selected) {
                int position = cardListViewAdapter.getPositionForKey(key);
                Log.d("SelectionTracker", "Item state changed. Key: " + key + ", Selected: " + selected);
                if (position != RecyclerView.NO_POSITION) {

                    boolean isCurrentlySelected = selectionTracker.isSelected(key);
                    if (isCurrentlySelected != selected) {
                        cardListViewAdapter.notifyDataSetChanged();
                    }
                    
                }
            }
        });
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

    private void toggleSelectionMode(boolean enable) {
        if (selectionTracker != null) {
            cardListViewAdapter.setSelectionModeEnabled(enable);
        }
        if (enable) {
            actionMode = getActivity().startActionMode(actionModeCallback);
        } else if (actionMode != null) {
            actionMode.finish();
        }
    }
    
}
