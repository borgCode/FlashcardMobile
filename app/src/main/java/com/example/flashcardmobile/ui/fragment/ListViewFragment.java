package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
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
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.ui.view.ListViewAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckCardViewModel;
import com.example.flashcardmobile.viewmodel.SharedViewModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
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

                if (id == R.id.search_list) {

                    SearchView searchView = (SearchView) menuItem.getActionView();
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
