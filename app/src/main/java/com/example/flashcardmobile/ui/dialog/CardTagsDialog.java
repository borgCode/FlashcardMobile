package com.example.flashcardmobile.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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


    private static final String ARG_TAG_ID = "card_id";

    public static CardTagsDialog newInstance(long cardId) {
        CardTagsDialog fragment = new CardTagsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_card_tag, container, false);

        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        tagContainer = view.findViewById(R.id.tag_container);

        tagInput = view.findViewById(R.id.tagInputBox);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        tagInput.setAdapter(adapter);

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

        clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> clearTags());


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

    public void clearTags() {
        for (Tag tag : selectedTagsMap.values()) {
            adapter.add(tag.getTagName());
            tagMap.put(tag.getTagName(), tag);
        }
        adapter.notifyDataSetChanged();
        selectedTagsMap.clear();
        tagContainer.removeAllViews();
    }
}
