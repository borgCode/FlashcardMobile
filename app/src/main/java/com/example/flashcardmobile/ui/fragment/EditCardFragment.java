package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.*;
import com.example.flashcardmobile.ui.dialog.TagDialog;
import com.example.flashcardmobile.util.TagHandler;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedDeckAndCardViewModel;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import com.google.android.flexbox.FlexboxLayout;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCardFragment extends Fragment {

    private CardViewModel cardViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private DeckViewModel deckViewModel;
    private TagViewModel tagViewModel;
    private EditText frontSide;
    private EditText backSide;
    private AutoCompleteTextView deckNameSelection;
    private Card card;
    private TagHandler tagHandler;
    private final Map<Long, Tag> selectedTagsMap = new HashMap<>();
    private final Map<String, Tag> tagMap = new HashMap<>();

    @Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_card, container, false);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Edit Card");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        card = sharedDeckAndCardViewModel.getSelectedCard().getValue();

        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        if (card != null ) {
            frontSide.setText(card.getFrontSide());
            backSide.setText(card.getBackSide());
        } else {
            
        }
        
        deckNameSelection = view.findViewById(R.id.deckSelectionAutoComplete);
        Map<String, Long> deckNameAndId = new HashMap<>();
        
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            deckNameAndId.clear();
            for (Deck deck: decks) {
                deckNameAndId.put(deck.getDeckName(), deck.getId());
            }
            List<String> deckNames = new ArrayList<>(deckNameAndId.keySet());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, deckNames);
            deckNameSelection.setAdapter(adapter);

            sharedDeckAndCardViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
                deckViewModel.getDeckById(deckId).observe(getViewLifecycleOwner(), deck -> {
                    if (deck != null) {
                        deckNameSelection.setText(deck.getDeckName(), false);
                    }
                });
            });
        });

        AutoCompleteTextView tagInput = view.findViewById(R.id.tagInputBox);
        FlexboxLayout tagContainer = view.findViewById(R.id.tag_container);
        tagHandler = new TagHandler(getContext(), tagInput, tagMap, selectedTagsMap, tagContainer);
        
        
        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), newTags -> {
            tagHandler.addAll(newTags);
        });
        
        tagViewModel.getCardWithTags(card.getId()).observe(getViewLifecycleOwner(), cardWithTags -> {
            cardWithTags.tags.forEach(tag -> tagHandler.addTagToContainer(tag));
        });
        

        ImageButton addTagButton = view.findViewById(R.id.create_tag_button);
        addTagButton.setOnClickListener(v -> createTag());

        Button clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> tagHandler.clearTags());

        Button saveButton = view.findViewById(R.id.addCardButton);
        saveButton.setText(R.string.save);

        saveButton.setOnClickListener(v -> saveCard());

        return view;
        
    }
    private void createTag() {
        TagDialog tagDialog = new TagDialog();
        tagDialog.show(getActivity().getSupportFragmentManager(), "createTag");
    }

    private void saveCard() {
        String newFrontSide = frontSide.getText().toString().trim();
        String newBackSide = backSide.getText().toString().trim();

        if (newFrontSide.isEmpty() || newBackSide.isEmpty()) {
            Toast.makeText(getActivity(),
                    "Make sure you input both a front side and back side for the card",
                    Toast.LENGTH_SHORT).show();
        } else {
            card.setFrontSide(newFrontSide);
            card.setBackSide(newBackSide);
            cardViewModel.update(card);

            List<CardTagCrossRef> crossRefs = new ArrayList<>();
            for (long tagId: selectedTagsMap.keySet()) {
                CardTagCrossRef crossRef = new CardTagCrossRef(card.getId(),tagId);
                crossRefs.add(crossRef);
            }
            tagViewModel.updateCrossRefs(card.getId(), crossRefs);
            tagHandler.clearTags();
            Toast.makeText(getActivity(), "Card updated", Toast.LENGTH_SHORT).show();
        }
        
    }
}
