package com.example.flashcardmobile.ui.fragment;

import android.graphics.Color;
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
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.entity.CardTagCrossRef;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.ui.dialog.CreateTagDialog;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.SharedViewModel;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddCardFragment extends Fragment {
    
    private CardViewModel cardViewModel;
    private SharedViewModel sharedViewModel;
    private TagViewModel tagViewModel;
    private EditText frontSide;
    private EditText backSide;
    private Button addButton;
    private AutoCompleteTextView tagInput;
    private ImageButton addTagButton;
    private LinearLayout tagContainer;
    private List<Long> selectedTagIds = new ArrayList<>();
    private Map<String, Tag> tagMap = new HashMap<>();

    @Nullable
    
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_card, container, false);
        
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Add Card");
        actionBar.setDisplayHomeAsUpEnabled(true);

        view.findViewById(R.id.deckSelectionBox).setVisibility(View.GONE);
        
        frontSide = view.findViewById(R.id.frontSideEditText);
        backSide = view.findViewById(R.id.backSideEditText);
        
        tagInput = view.findViewById(R.id.tagInputBox);
        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), newTags -> {
            tagMap.clear();
            tagMap = newTags.stream().collect(Collectors.toMap(Tag::getTagName, tag -> tag, (existing, replacement) -> existing, HashMap::new));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>(tagMap.keySet()));
            tagInput.setAdapter(adapter);
        });
        tagInput.setOnItemClickListener(((parent, view1, position, id) -> {
            String selectedTagName = (String) parent.getItemAtPosition(position);
            Tag selectedTag = tagMap.get(selectedTagName);
            
            selectedTagIds.add(selectedTag.getId());
            
            addTagToContainer(selectedTag);
            
        }));
        
        addTagButton = view.findViewById(R.id.create_tag_button);
        addTagButton.setOnClickListener(v -> createTag());
        
        tagContainer = view.findViewById(R.id.tag_container);
        
        
        addButton = view.findViewById(R.id.addCardButton);
        addButton.setOnClickListener(v -> addCard());
        
        return view;
        
    }

    private void addTagToContainer(Tag selectedTag) {
        TextView tagView = new TextView(getActivity());
        tagView.setBackgroundResource(R.drawable.tag_bubble_background);
        tagView.setText(selectedTag.getTagName());
        tagView.setBackgroundColor(selectedTag.getColor());
        tagView.setTextColor(Color.WHITE);
        
        tagContainer.addView(tagView);
    }

    private void createTag() {
        CreateTagDialog createTagDialog = new CreateTagDialog();
        createTagDialog.show(getActivity().getSupportFragmentManager(), "createTag");
    }

    private void addCard() {
        String frontText = frontSide.getText().toString().trim();
        String backText = backSide.getText().toString().trim();
        long deckId = sharedViewModel.getDeckId().getValue();

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
            
            List<CardTagCrossRef> crossRefs = new ArrayList<>();

            for (long tagId: selectedTagIds) {
                CardTagCrossRef crossRef = new CardTagCrossRef(card.getId(),tagId);
                crossRefs.add(crossRef);
            }
            tagViewModel.insertCrossRefs(crossRefs);
        }
    }
}
