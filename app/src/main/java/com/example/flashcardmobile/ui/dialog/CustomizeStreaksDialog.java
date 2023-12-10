package com.example.flashcardmobile.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.flashcardmobile.R;

public class 
CustomizeStreaksDialog extends DialogFragment {
    
    public interface OnButtonSelectedListener {
        void onDailyStreakSelected();
        void onWeeklyStreakSelected(int selectedDays);
    }
    
    private OnButtonSelectedListener listener;

    public void setButtonSelectedListener(OnButtonSelectedListener listener) {
        this.listener = listener;
    }
    
        
   

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_customize_streaks, null);
        
        Spinner spinner = view.findViewById(R.id.spinner);

        RadioGroup streakGroup = view.findViewById(R.id.streak_radio_group);
        
        streakGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            if (checkedId == R.id.weekly_streak_button) {
                spinner.setVisibility(View.VISIBLE);
            } else {
                spinner.setVisibility(View.GONE);
            }
        }));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), 
                R.array.streak_numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Customize Streak Goals")
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedId = streakGroup.getCheckedRadioButtonId();
                    if (selectedId == R.id.weekly_streak_button) {
                        int selectedDays = Integer.parseInt(spinner.getSelectedItem().toString());
                        listener.onWeeklyStreakSelected(selectedDays);
                    } else {
                        listener.onDailyStreakSelected();
                    }
                    
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    getDialog().cancel();
                });
        
        return builder.create();
    }
}
