package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment implements CardAdapter.AdapterCallback {
    private CardViewModel cardViewModel;
    private List<Card> cards;
    private CardAdapter cardAdapter;
    private ViewPager2 viewPager2;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        viewPager2 = view.findViewById(R.id.practiceView);
        viewPager2.setUserInputEnabled(false);
        cards = new ArrayList<>();
        cardAdapter = new CardAdapter(cards, this);
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

    @Override
    public void onDifficultySelected(Card card, int difficulty) {
        Card newCard = calculateDueDate(card, difficulty);
        cardViewModel.update(newCard);
    }
    private Card calculateDueDate(Card card, int difficulty) {
        
        int repetitions = card.getRepetitions();
        double easiness = card.getEasiness();
        int interval = card.getInterval();
        
        easiness = Math.max(1.3, easiness + 0.1 - (5.0 - difficulty)
                * (0.08 + (5.0 - difficulty) * 0.02));
        
        if (difficulty < 3) {
            repetitions = 0;
        } else {
            repetitions += 1;
        }
        
        if (repetitions <= 1) {
            interval = 1;
        } else if (repetitions == 2) {
            interval = 6;
        } else {
            interval = (int) Math.round(interval * easiness);
        }
        card.setEasiness(easiness);
        card.setInterval(interval);
        card.setRepetitions(repetitions);

        LocalDateTime now = LocalDateTime.now();
        card.setDueDate(now.plusDays(interval));
        
        return card;
        
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

