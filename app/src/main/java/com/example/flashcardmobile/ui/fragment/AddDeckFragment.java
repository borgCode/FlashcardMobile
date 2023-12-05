package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import org.jetbrains.annotations.NotNull;

public class AddDeckFragment extends Fragment {
    private DeckViewModel deckViewModel;
    private EditText deckName;
    private Button addButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_deck, container, false);

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        deckName = view.findViewById(R.id.add_deck_name);
        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> addDeck());

        return view;
    }

    private void addDeck() {
        String name = deckName.getText().toString().trim();

        if (!name.isEmpty()) {
            Deck newDeck = new Deck(name);
            deckViewModel.insert(newDeck);
            deckName.setText("");
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        } else {
            Toast.makeText(getActivity(), "Please insert a deck name", Toast.LENGTH_SHORT).show();
        }

    }
}
