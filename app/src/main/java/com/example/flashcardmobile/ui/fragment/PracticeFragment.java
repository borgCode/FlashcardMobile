package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.ui.view.CardAdapter;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import com.example.flashcardmobile.viewmodel.SharedPracticeViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {
    private CardViewModel cardViewModel;
    private List<Card> cards;
    private CardAdapter cardAdapter;
    private ViewPager2 viewPager2;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        viewPager2 = view.findViewById(R.id.practiceView);
        viewPager2.setUserInputEnabled(false);
        cards = new ArrayList<>();
        cardAdapter = new CardAdapter(cards);
        viewPager2.setAdapter(cardAdapter);


        SharedPracticeViewModel sharedPracticeViewModel = new ViewModelProvider(requireActivity())
                .get(SharedPracticeViewModel.class);
        sharedPracticeViewModel.getId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getDueCards(deckId).observe(getViewLifecycleOwner(), newCards ->  {
                cards.clear();
                cards.addAll(newCards);
                cardAdapter.notifyDataSetChanged();
            });

        });

        sharedPracticeViewModel.getName().observe(getViewLifecycleOwner(), deckName -> {
            if (actionBar != null) {
                actionBar.setTitle(deckName);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        });
        
        return view;
    }

    



//        requireActivity().addMenuProvider(new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
//                Log.d("Practice Toolbar", "creating toolbar");
//                menuInflater.inflate(R.menu.menu_practice_toolbar, menu);
//                Log.d("Practice Toolbar", "toolbar created");
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
//                return false;
//            }
//        });
    }

