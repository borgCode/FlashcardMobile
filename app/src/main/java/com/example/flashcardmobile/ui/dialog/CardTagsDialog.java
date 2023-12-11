package com.example.flashcardmobile.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CardTagsDialog extends DialogFragment {

    private TagViewModel tagViewModel;

    private AutoCompleteTextView tagInput;
    private Button clearButton;
    private LinearLayout tagContainer;
    private Map<Long, Tag> selectedTagsMap = new HashMap<>();
    private Map<String, Tag> tagMap = new HashMap<>();
    private ArrayAdapter<String> adapter;

    private DisplayMetrics metrics;
    private int screenWidth;
    private int screenHeight;


    private static final String ARG_TAG_ID = "card_id";

    public static CardTagsDialog newInstance(long cardId) {
        CardTagsDialog fragment = new CardTagsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
   
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_card_tag, container, false);

        metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        tagContainer = view.findViewById(R.id.tag_container);

        setupAutoCompleteTextView(view);
        

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), tags -> {
            tagMap.clear();
            tagMap = tags.stream().collect(Collectors.toMap(Tag::getTagName, tag -> tag, (existing, replacement) -> existing, HashMap::new));
            adapter.clear();
            adapter.addAll(new ArrayList<>(tagMap.keySet()));
            adapter.notifyDataSetChanged();
        });

        tagViewModel.getCardWithTags(getArguments().getLong(ARG_TAG_ID)).observe(getViewLifecycleOwner(), card -> {
            for (Tag tag: card.tags) {
                addTagToContainer(tag);
            }
        });

        clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> clearTags());

        return view;

    }

    private void setupAutoCompleteTextView(View view) {
        tagInput = view.findViewById(R.id.tagInputBox);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        tagInput.setAdapter(adapter);
        tagInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tagInput.showDropDown();
            }
        });
        
        int dropdownHeight = screenHeight / 5;
        tagInput.setDropDownHeight(dropdownHeight);
        
        tagInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    tagInput.showDropDown();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
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
        
        
    }

    @Override
    public void onStart() {
        super.onStart();
        
        Dialog dialog = this.getDialog();
        dialog.getWindow().setLayout((6 * screenWidth)/7, (2 * screenHeight/5));
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

    public void clearTags() {
        for (Tag tag : selectedTagsMap.values()) {
            adapter.add(tag.getTagName());
            tagMap.put(tag.getTagName(), tag);
        }
        adapter.notifyDataSetChanged();
        selectedTagsMap.clear();
        tagContainer.removeAllViews();
    }
    
    //TODO UPDATE DB LOGIC
}
