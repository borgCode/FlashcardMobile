package com.example.flashcardmobile.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.entity.CardTagCrossRef;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.ui.dialog.TagDialog;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.SharedAnalyticsViewModel;
import com.example.flashcardmobile.viewmodel.SharedDeckAndCardViewModel;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import com.google.android.flexbox.FlexboxLayout;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AddCardFragment extends Fragment {
    
    private CardViewModel cardViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private SharedAnalyticsViewModel sharedAnalyticsViewModel;
    private TagViewModel tagViewModel;
    private EditText frontSide;
    private EditText backSide;
    private Button addButton;
    private AutoCompleteTextView tagInput;
    private ImageButton addTagButton;
    private Button clearButton;
    private FlexboxLayout tagContainer;
    private Map<Long, Tag> selectedTagsMap = new HashMap<>();
    private Map<String, Tag> tagMap = new HashMap<>();
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private int cardsAdded = 0;

    @Nullable
    
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_card, container, false);
        
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Add Card");
        actionBar.setDisplayHomeAsUpEnabled(true);

        view.findViewById(R.id.deckSelectionBox).setVisibility(View.GONE);

        sharedPreferences = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);
        sharedAnalyticsViewModel = new ViewModelProvider(requireActivity()).get(SharedAnalyticsViewModel.class);
        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);
        
        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        
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
        
        addTagButton = view.findViewById(R.id.create_tag_button);
        addTagButton.setOnClickListener(v -> createTag());
        
        tagContainer = view.findViewById(R.id.tag_container);
        
        clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> {
            clearTags();
        });
        
        addButton = view.findViewById(R.id.addCardButton);
        addButton.setOnClickListener(v -> addCard());
        
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
        TagDialog tagDialog = new TagDialog();
        tagDialog.show(getActivity().getSupportFragmentManager(), "createTag");
    }

    public void clearTags() {
        for (Tag tag: selectedTagsMap.values()) {
            adapter.add(tag.getTagName());
            tagMap.put(tag.getTagName(), tag);
        }
        adapter.notifyDataSetChanged();
        selectedTagsMap.clear();
        tagContainer.removeAllViews();
    }

    private void addCard() {
        String frontText = frontSide.getText().toString().trim();
        String backText = backSide.getText().toString().trim();
        long deckId = sharedDeckAndCardViewModel.getDeckId().getValue();
        
        if (frontText.isEmpty() || backText.isEmpty()) {
            Toast.makeText(getActivity(),
                    "Make sure you input both a front side and back side for the card",
                    Toast.LENGTH_SHORT).show();
        } else {
            Card card = new Card(deckId, frontText, backText, LocalDateTime.now(),
                    0, 1, 2.5);
            frontSide.setText("");
            backSide.setText("");
            tagInput.setText("");
            
            List<CardTagCrossRef> crossRefs = new ArrayList<>();

            cardViewModel.insert(card).thenAcceptAsync(cardId -> {
                for (Long tagId: selectedTagsMap.keySet()) {
                    CardTagCrossRef crossRef = new CardTagCrossRef(cardId,tagId);
                    crossRefs.add(crossRef);
                }
                tagViewModel.insertCrossRefs(crossRefs).thenRun(() -> {
                    clearTags();
                    Toast.makeText(getActivity(), "Card added!", Toast.LENGTH_SHORT).show();
                    cardsAdded++;
                    saveNumOfCardsAdded(cardsAdded);
                });
            }, Executors.newSingleThreadExecutor());
        }
    }

    private void saveNumOfCardsAdded(int cardsAdded) {
        editor.putInt("cardsAdded", cardsAdded);
        editor.apply();
    }
    

    @Override
    public void onResume() {
        super.onResume();
        saveAndResetSession(cardsAdded);
    }


    @Override
    public void onPause() {
        super.onPause();
        saveAndResetSession(cardsAdded);
    }

    private void saveAndResetSession(int cardsAdded) {
        sharedAnalyticsViewModel.updateCardsAdded(cardsAdded);
        editor.remove("cardsAdded");
        editor.apply();
    }


}
