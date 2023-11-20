package com.example.flashcardmobile.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class CreateTagDialog extends DialogFragment {

    TagViewModel tagViewModel;
    int defaultColor;
    EditText textInput;
    Button colorPickerBtn;
    TextView tagPreview;
    Button createBtn;

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
        createBtn.setOnClickListener(l -> createTag());

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

        textInput.setText("");
        

    }


}

