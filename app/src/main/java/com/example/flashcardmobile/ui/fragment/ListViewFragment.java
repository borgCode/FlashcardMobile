package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
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
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.ui.view.ListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;
import com.example.flashcardmobile.viewmodel.SharedViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment implements ListViewAdapter.onCardOperationListener {

    private DeckCardViewModel deckCardViewModel;
    private CardViewModel cardViewModel;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private ListViewAdapter listViewAdapter;
    private List<DeckCard> cards;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        cards = new ArrayList<>();

        deckCardViewModel = new ViewModelProvider(requireActivity()).get(DeckCardViewModel.class);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewAdapter = new ListViewAdapter(this);
        recyclerView.setAdapter(listViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));

        deckCardViewModel.getAllCards().observe(getViewLifecycleOwner(), newCards -> {
            listViewAdapter.setCards(new ArrayList<>(newCards));
            listViewAdapter.setCardsFull(new ArrayList<>(newCards));
            listViewAdapter.notifyDataSetChanged();
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
                            String selectedOption = parent.getItemAtPosition(position).toString();
                            listViewAdapter.setCurrentSearchColumn(selectedOption);
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
                            listViewAdapter.getFilter().filter(newText);
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
                sharedViewModel.setSelectedCard(card);
                sharedViewModel.setDeckId(deckId);
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
    public void onResetDueDate(int position) {
        

    }

    @Override
    public void onCardDelete(long cardId) {

    }
}
