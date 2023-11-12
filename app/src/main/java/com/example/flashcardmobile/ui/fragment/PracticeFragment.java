package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;
import org.jetbrains.annotations.NotNull;

public class PracticeFragment extends Fragment {
    private CardViewModel cardViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPracticeViewModel sharedPracticeViewModel = new ViewModelProvider(requireActivity())
                .get(SharedPracticeViewModel.class);
        sharedPracticeViewModel.getId().observe(this, deckId -> {
            
        });
        
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        
        
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                Log.d("Practice Toolbar", "creating toolbar");
                menuInflater.inflate(R.menu.menu_practice_toolbar, menu);
                Log.d("Practice Toolbar", "toolbar created");
            }

            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
                return false;
            }
        });
    }
}
