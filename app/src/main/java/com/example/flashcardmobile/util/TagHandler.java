package com.example.flashcardmobile.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Tag;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagHandler {
    
    
    private final Context context;
    private final AutoCompleteTextView tagInput;
    private final ArrayAdapter<String> adapter;
    private final Map<String, Tag> tagMap;
    private final Map<Long, Tag> selectedTagsMap;
    private final FlexboxLayout tagContainer;

    public TagHandler(Context context, AutoCompleteTextView tagInput, Map<String, Tag> tagMap, Map<Long, Tag> selectedTagsMap, FlexboxLayout tagContainer) {
        this.context = context.getApplicationContext();
        this.tagInput = tagInput;
        this.tagMap = tagMap;
        this.adapter = new ArrayAdapter<>(tagInput.getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        this.selectedTagsMap = selectedTagsMap;
        this.tagContainer = tagContainer;
        tagInput.setAdapter(adapter);
        setupTagInput();
    }

    private void setupTagInput() {
        tagInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tagInput.showDropDown();
            }
        });

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

            adapter.remove(selectedTagName);
            adapter.notifyDataSetChanged();

            selectedTagsMap.put(selectedTag.getId(), selectedTag);
            addTagToContainer(selectedTag);

            tagInput.setText("");

        }));
        
    }

    public void setupAutoCompleteTextViewSize(int screenHeight) {
        int dropdownHeight = screenHeight / 5;
        tagInput.setDropDownHeight(dropdownHeight);
    }

    public void addAll(List<Tag> tags) {
        tagMap.clear();
        tagMap.putAll(tags.stream().collect(Collectors.toMap(Tag::getTagName, tag -> tag, (existing, replacement) -> existing)));
        adapter.clear();
        adapter.addAll(new ArrayList<>(tagMap.keySet()));
        adapter.notifyDataSetChanged();
    }

    public void addTagToContainer(Tag selectedTag) {
        View tagView = LayoutInflater.from(context).inflate(R.layout.tag_layout, null);
        TextView tagText = tagView.findViewById(R.id.tag_text);
        
        ImageView closeButton = tagView.findViewById(R.id.tag_close_button);

        GradientDrawable background = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.tag_bubble_background).mutate();
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

        adapter.remove(selectedTag.getTagName());
        adapter.notifyDataSetChanged();

        selectedTagsMap.put(selectedTag.getId(), selectedTag);
        tagContainer.addView(tagView);
    }

    public void clearTags() {
        for (Tag tag : selectedTagsMap.values()) {
            adapter.add(tag.getTagName());
        }

        adapter.notifyDataSetChanged();
        adapter.getFilter().filter(tagInput.getText(), null);
        selectedTagsMap.clear();
        tagContainer.removeAllViews();
    }
}
