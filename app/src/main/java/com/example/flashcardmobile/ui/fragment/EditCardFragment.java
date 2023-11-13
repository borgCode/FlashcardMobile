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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;
import org.jetbrains.annotations.NotNull;

public class EditCardFragment extends Fragment {

    private CardViewModel cardViewModel;
    SharedPracticeViewModel sharedPracticeViewModel;
    private EditText frontSide;
    private EditText backSide;
    private EditText tags;
    private Button saveButton;
    private Card card;

    @Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_card, container, false);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Edit Card");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedPracticeViewModel = new ViewModelProvider(requireActivity()).get(SharedPracticeViewModel.class);
        card = sharedPracticeViewModel.getSelectedCard().getValue();

        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        frontSide.setText(card.getFrontSide());
        backSide.setText(card.getBackSide());

        //TODO implement tags in db 
        tags = view.findViewById(R.id.tagsEditText);
        saveButton = view.findViewById(R.id.addCardButton);
        saveButton.setText(R.string.save);

        saveButton.setOnClickListener(v -> saveCard());

        return view;
        
    }

    private void saveCard() {
        String newFrontSide = frontSide.getText().toString().trim();
        String newBackSide = backSide.getText().toString().trim();

        if (newFrontSide.isEmpty() || newBackSide.isEmpty()) {
            Log.d("EditCardF", "Fields not filled");
            Toast.makeText(getActivity(),
                    "Make sure you input both a front side and back side for the card",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.d("EditCardF", "Adding card to DB");
            card.setFrontSide(newFrontSide);
            card.setBackSide(newBackSide);
            cardViewModel.update(card);
            Toast.makeText(getActivity(), "Card updated", Toast.LENGTH_SHORT).show();
            Log.d("EditCardF", "Fields set to blank");
        }
    }
}
