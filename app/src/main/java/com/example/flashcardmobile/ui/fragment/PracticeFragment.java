package com.example.flashcardmobile.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
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
    private SharedPracticeViewModel sharedPracticeViewModel;
    private List<Card> cards;
    private CardAdapter cardAdapter;
    private ViewPager2 viewPager2;
    private int currentCardPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedPracticeViewModel = new ViewModelProvider(requireActivity()).get(SharedPracticeViewModel.class);
        
        viewPager2 = view.findViewById(R.id.practiceView);
        viewPager2.setUserInputEnabled(false);
        cards = new ArrayList<>();
        cardAdapter = new CardAdapter(cards, this);
        viewPager2.setAdapter(cardAdapter);
        
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentCardPosition = position;
            }
        });
        
        sharedPracticeViewModel.getId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getDueCards(deckId).observe(getViewLifecycleOwner(), newCards -> {
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
        requireActivity().addMenuProvider(new MenuProvider() {
            
            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                Log.d("Practice Toolbar", "creating toolbar");
                menuInflater.inflate(R.menu.menu_practice_dropdown, menu);
                Log.d("Practice Toolbar", "toolbar created");
            }
            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.editCardItem) {
                    if (currentCardPosition != -1 && currentCardPosition < cards.size()) {
                        Card cardToEdit = cards.get(currentCardPosition);
                        sharedPracticeViewModel.setSelectedCard(cardToEdit);
                        EditCardFragment editCardFragment = new EditCardFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, editCardFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        return view;
    }

    @Override
    public void onDifficultySelected(Card card, int difficulty) {
        Log.d("Due date calculation", "Calling calculateduedate method");
        Card newCard = calculateDueDate(card, difficulty);
        cardViewModel.update(newCard);
        Log.d("Update new Due Date", "due date updated");
    }

    private Card calculateDueDate(Card card, int difficulty) {

        int repetitions = card.getRepetitions();
        double easiness = card.getEasiness();
        int interval = card.getInterval();

        Log.d("Due date calculation", "Caclulate easiness, current is " + easiness);

        easiness = Math.max(1.3, easiness + 0.1 - (5.0 - difficulty)
                * (0.08 + (5.0 - difficulty) * 0.02));

        Log.d("Due date calculation", "new easiness: " + easiness);


        if (difficulty >= 3) {
            repetitions += 1;
        } else {
            repetitions = 0;
        }

        if (repetitions == 0) {
            interval = 1;
        } else if (repetitions == 1) {
            interval = 6;
        } else {
            interval = (int) Math.round(interval * easiness);
        }
        Log.d("Due date calculation", "new interval value: " + interval + "" +
                "\n Setting new values");
        card.setEasiness(easiness);
        card.setInterval(interval);
        card.setRepetitions(repetitions);

        LocalDateTime now = LocalDateTime.now();
        card.setDueDate(now.plusDays(interval));

        Log.d("Due date calculation", "Returning new card");

        return card;

    }
    

}

