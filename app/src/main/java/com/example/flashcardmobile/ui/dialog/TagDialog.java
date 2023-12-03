package com.example.flashcardmobile.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.viewmodel.TagViewModel;
import org.jetbrains.annotations.NotNull;
import yuku.ambilwarna.AmbilWarnaDialog;

public class TagDialog extends DialogFragment {

    private TagViewModel tagViewModel;
    private int defaultColor;
    private EditText textInput;
    private Button colorPickerBtn;
    private TextView tagPreview;
    private Button createBtn;
    private static final String ARG_TAG_ID = "tag_id";
    private static final String ARG_TAG_NAME = "tag_name";
    private static final String ARG_TAG_COLOR = "tag_color";
    private boolean isEditMode = false;

    public static TagDialog newInstance(long tagId, String tagName, int tagColor) {
        TagDialog fragment = new TagDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, tagId);
        args.putString(ARG_TAG_NAME, tagName);
        args.putInt(ARG_TAG_COLOR, tagColor);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_tag, container, false);

        tagViewModel = new ViewModelProvider(requireActivity()).get(TagViewModel.class);

        defaultColor = Color.rgb(0, 0, 0);

        textInput = view.findViewById(R.id.tagInputText);
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagPreview.setText(textInput.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        

        colorPickerBtn = view.findViewById(R.id.dialog_open_color_picker);
        colorPickerBtn.setOnClickListener(l -> openColorPicker());

        tagPreview = view.findViewById(R.id.tagPreview);
        tagPreview.setBackgroundColor(defaultColor);

        createBtn = view.findViewById(R.id.dialog_create_tag_button);

        if (getArguments() != null && getArguments().containsKey(ARG_TAG_NAME)) {
            isEditMode = true;
            String tagName = getArguments().getString(ARG_TAG_NAME);
            defaultColor = getArguments().getInt(ARG_TAG_COLOR);
            textInput.setText(tagName);
            tagPreview.setBackgroundColor(defaultColor);
            createBtn.setText("Save Changes");
        }
        
        createBtn.setOnClickListener(l -> {
            if (isEditMode) {
                updateTag();
            } else {
                createTag();
            }
        });

        return view;
    }


    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                tagPreview.setBackgroundColor(defaultColor);
            }
        });
        colorPicker.show();
    }

    private void createTag() {
        tagViewModel.insert(new Tag(textInput.getText().toString(), defaultColor));
        dismiss();
    }

    private void updateTag() {
        tagViewModel.getTagById(getArguments().getLong(ARG_TAG_ID)).observe(getViewLifecycleOwner(), tag -> {
            tag.setTagName(textInput.getText().toString());
            tag.setColor(defaultColor);
            tagViewModel.update(tag);
        });
    }
    
}

