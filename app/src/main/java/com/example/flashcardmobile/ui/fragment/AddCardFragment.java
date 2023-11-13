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

import java.time.LocalDateTime;

public class AddCardFragment extends Fragment {
    
    private CardViewModel cardViewModel;
    private SharedPracticeViewModel sharedPracticeViewModel;
    private EditText frontSide;
    private EditText backSide;
    private EditText tags;
    private Button addButton;

    @Nullable
    
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);
        
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedPracticeViewModel = new ViewModelProvider(requireActivity()).get(SharedPracticeViewModel.class);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Add Card");
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        //TODO implement tags in db 
        tags = view.findViewById(R.id.tagsEditText);
        addButton = view.findViewById(R.id.addCardButton);
        
        addButton.setOnClickListener(v -> addCard());
        
        return view;
        
    }

    private void addCard() {
        String frontText = frontSide.getText().toString().trim();
        String backText = backSide.getText().toString().trim();
        long deckId = sharedPracticeViewModel.getId().getValue();

        Log.d("AddCardF", "Checking if fields are filled");
        
        if (frontText.isEmpty() || backText.isEmpty()) {
            Log.d("AddCardF", "Fields not filled");
            Toast.makeText(getActivity(),
                    "Make sure you input both a front side and back side for the card",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.d("AddCardF", "Adding card to DB");
            Card card = new Card(deckId, frontText, backText, LocalDateTime.now(),
                    0, 1, 2.5);
            cardViewModel.insert(card);
            frontSide.setText("");
            backSide.setText("");
            Log.d("AddCardF", "Fields set to blank");
        }
        
    }
}
