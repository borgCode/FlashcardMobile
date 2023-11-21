package com.example.flashcardmobile.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.*;
import com.example.flashcardmobile.ui.dialog.CreateTagDialog;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedViewModel;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditCardFragment extends Fragment {

    private CardViewModel cardViewModel;
    private SharedViewModel sharedViewModel;
    private DeckViewModel deckViewModel;
    private TagViewModel tagViewModel;
    private EditText frontSide;
    private EditText backSide;
    private AutoCompleteTextView deckNameSelection;
    private Button saveButton;
    private Button clearButton;
    private Card card;
    private AutoCompleteTextView tagInput;
    private ImageButton addTagButton;
    private LinearLayout tagContainer;
    private Map<Long, Tag> selectedTagsMap = new HashMap<>();
    private Map<String, Tag> tagMap = new HashMap<>();
    private ArrayAdapter<String> adapter;

    @Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_card, container, false);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Edit Card");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        
        card = sharedViewModel.getSelectedCard().getValue();
        
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

            sharedViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
                deckViewModel.getDeckById(deckId).observe(getViewLifecycleOwner(), deck -> {
                    if (deck != null) {
                        deckNameSelection.setText(deck.getDeckName(), false);
                    }
                });
            });
        });
        
        tagInput = view.findViewById(R.id.tagInputBox);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        tagInput.setAdapter(adapter);
        
        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), newTags -> {
            tagMap.clear();
            tagMap = newTags.stream().collect(Collectors.toMap(Tag::getTagName, tag -> tag, (existing, replacement) -> existing, HashMap::new));
            adapter.clear();
            adapter.addAll(new ArrayList<>(tagMap.keySet()));
            adapter.notifyDataSetChanged();
        });
        tagInput.setOnItemClickListener(((parent, view1, position, id) -> {
            String selectedTagName = (String) parent.getItemAtPosition(position);
            Tag selectedTag = tagMap.get(selectedTagName);
            
            tagMap.remove(selectedTagName);
            adapter.remove(selectedTagName);
            adapter.notifyDataSetChanged();
            
            selectedTagsMap.put(selectedTag.getId(), selectedTag);
            addTagToContainer(selectedTag);
            
            tagInput.setText("");
        }));

        tagContainer = view.findViewById(R.id.tag_container);
        tagViewModel.getCardWithTags(card.getId()).observe(getViewLifecycleOwner(), cardWithTags -> {
            cardWithTags.tags.forEach(tag -> {
                selectedTagsMap.put(tag.getId(), tag);
                addTagToContainer(tag);
                tagMap.remove(tag.getTagName());
            });
        });
        
        adapter.clear();
        adapter.addAll(new ArrayList<>(tagMap.keySet()));
        adapter.notifyDataSetChanged();

        addTagButton = view.findViewById(R.id.create_tag_button);
        addTagButton.setOnClickListener(v -> createTag());

        clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> {
            for (Tag tag: selectedTagsMap.values()) {
                adapter.add(tag.getTagName());
                tagMap.put(tag.getTagName(), tag);
            }
            adapter.notifyDataSetChanged();
            selectedTagsMap.clear();
            tagContainer.removeAllViews();
        });
        
        saveButton = view.findViewById(R.id.addCardButton);
        saveButton.setText(R.string.save);

        saveButton.setOnClickListener(v -> saveCard());

        return view;
        
    }
    

    private void addTagToContainer(Tag selectedTag) {
        View tagView = getLayoutInflater().inflate(R.layout.tag_layout, null);
        TextView tagText = tagView.findViewById(R.id.tag_text);
        ImageView closeButton = tagView.findViewById(R.id.tag_close_button);

        GradientDrawable background = (GradientDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.tag_bubble_background).mutate();
        background.setColor(selectedTag.getColor());

        tagText.setBackground(background);
        tagText.setText(selectedTag.getTagName());
        tagText.setTextColor(Color.WHITE);

        closeButton.setOnClickListener(l -> {
            tagContainer.removeView(tagView);
            tagMap.put(selectedTag.getTagName(), selectedTag);
            adapter.add(selectedTag.getTagName());
            adapter.notifyDataSetChanged();
            selectedTagsMap.remove(selectedTag.getId());
        });


        tagContainer.addView(tagView);
    }

    private void createTag() {
        CreateTagDialog createTagDialog = new CreateTagDialog();
        createTagDialog.show(getActivity().getSupportFragmentManager(), "createTag");
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
            Toast.makeText(getActivity(), "Card updated", Toast.LENGTH_SHORT).show();
        }
    }
}
