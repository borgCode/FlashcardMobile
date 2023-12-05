package com.example.flashcardmobile.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import org.jetbrains.annotations.NotNull;

public class EditDeckNameDialog extends DialogFragment {
    private DeckViewModel deckViewModel;
    private EditText editText;
    private Button saveButton;

    private static final String ARG_DECK_ID = "deck_id";
    private static final String ARG_DECK_NAME = "deck_name";

    public static EditDeckNameDialog newInstance(long deckId, String deckName) {
        EditDeckNameDialog fragment = new EditDeckNameDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_DECK_ID, deckId);
        args.putString(ARG_DECK_NAME, deckName);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_single_textfield, container, false);

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        editText = view.findViewById(R.id.edit_text);
        editText.setText(getArguments().getString(ARG_DECK_NAME));
        
        saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> saveChange());

        return view;
    }

    private void saveChange() {
        String newName = editText.getText().toString().trim();
        
        if (!newName.isEmpty()) {
            deckViewModel.updateDeckName(getArguments().getLong(ARG_DECK_ID), newName);
            
        } else {
            Toast.makeText(getActivity(), "Please insert a deck name", Toast.LENGTH_SHORT).show();
        }

    }
}
