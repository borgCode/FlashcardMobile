package com.example.flashcardmobile.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.util.TagHandler;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import com.google.android.flexbox.FlexboxLayout;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CardTagsDialog extends DialogFragment{

    public interface OnButtonSelectedListener {
        void onTagsChanged(long id, List<Tag> tags);
    }

    private OnButtonSelectedListener listener;

    private Map<Long, Tag> selectedTagsMap = new HashMap<>();
    private Map<String, Tag> tagMap = new HashMap<>();
    private TagHandler tagHandler;
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

    public void setButtonSelectedListener(CardTagsDialog.OnButtonSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_card_tag, container, false);
        
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        TagViewModel tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        FlexboxLayout tagContainer = view.findViewById(R.id.tag_container);

        AutoCompleteTextView tagInput = view.findViewById(R.id.tagInputBox);
        tagHandler = new TagHandler(getContext(), tagInput, tagMap,selectedTagsMap, tagContainer);
        tagHandler.setupAutoCompleteTextViewSize(screenHeight);

        tagViewModel.getAllTags().observe(getViewLifecycleOwner(), tags -> {
            tagHandler.addAll(tags);
        });

        tagViewModel.getCardWithTags(getArguments().getLong(ARG_TAG_ID)).observe(getViewLifecycleOwner(), card -> {
            for (Tag tag : card.tags) {
                tagHandler.addTagToContainer(tag);
            }
        });

        Button clearButton = view.findViewById(R.id.clear_tags_button);
        clearButton.setOnClickListener(v -> tagHandler.clearTags());

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> getDialog().cancel());

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            listener.onTagsChanged(getArguments().getLong("card_id"), new ArrayList<>(selectedTagsMap.values()));
            getDialog().cancel();
        });
        
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();
        dialog.getWindow().setLayout((6 * screenWidth) / 7, (2 * screenHeight / 5));
    }
}
