package com.example.flashcardmobile.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.ui.view.CardAdapter;
import com.example.flashcardmobile.viewmodel.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment implements CardAdapter.AdapterCallback {
    private CardViewModel cardViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private DeckViewModel deckViewModel;
    private StudySessionViewModel studySessionViewModel;
    private SharedAnalyticsViewModel sharedAnalyticsViewModel;
    private List<Card> cards;
    private CardAdapter cardAdapter;
    private ViewPager2 viewPager2;
    private int currentCardPosition = -1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int numOfCardsStudied = 0;
    private int numOfCardsMastered = 0;
    private int easyCounter;
    private int mediumCounter;
    private int hardCounter;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        sharedPreferences = getActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        sharedDeckAndCardViewModel = new ViewModelProvider(requireActivity()).get(SharedDeckAndCardViewModel.class);
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        studySessionViewModel = new ViewModelProvider(requireActivity()).get(StudySessionViewModel.class);
        sharedAnalyticsViewModel = new ViewModelProvider(requireActivity()).get(SharedAnalyticsViewModel.class);

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

        sharedDeckAndCardViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getDueCards(deckId).observe(getViewLifecycleOwner(), newCards -> {
                cards.clear();
                cards.addAll(newCards);
                cardAdapter.notifyDataSetChanged();
            });
            deckViewModel.getDeckById(deckId).observe(getViewLifecycleOwner(), deck -> {
                if (actionBar != null) {
                    actionBar.setTitle(deck.getDeckName());
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            });

        });
        requireActivity().addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_practice_dropdown, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull @NotNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.editCardItem) {
                    if (currentCardPosition != -1 && currentCardPosition < cards.size()) {
                        Card cardToEdit = cards.get(currentCardPosition);
                        sharedDeckAndCardViewModel.setSelectedCard(cardToEdit);
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
        Card newCard = calculateDueDate(card, difficulty);
        cardViewModel.update(newCard);
        numOfCardsStudied++;
        saveCardAnalytics("cardsStudied", numOfCardsStudied);
        switch (difficulty) {
            case 1: easyCounter++;
                saveCardAnalytics("easy", easyCounter);
            break;
            case 3: mediumCounter++;
                saveCardAnalytics("medium", mediumCounter);
            break;
            case 5: hardCounter++;
            saveCardAnalytics("hard", hardCounter);
            break;
        }
    }

    private Card calculateDueDate(Card card, int difficulty) {

        int repetitions = card.getRepetitions();
        double easiness = card.getEasiness();
        int interval = card.getInterval();

        easiness = Math.max(1.3, easiness + 0.1 - (5.0 - difficulty)
                * (0.08 + (5.0 - difficulty) * 0.02));

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

        card.setEasiness(easiness);
        card.setInterval(interval);
        card.setRepetitions(repetitions);

        LocalDateTime now = LocalDateTime.now();
        card.setDueDate(now.plusDays(interval));

        if (card.getDueDate().isAfter(now.plusDays(180))) {
            numOfCardsMastered++;
            saveCardAnalytics("cardsMastered", numOfCardsMastered);
        }

        return card;

    }

    @Override
    public void onResume() {
        super.onResume();
        long startTime = System.currentTimeMillis();
        saveAndResetSession(startTime, numOfCardsStudied, numOfCardsMastered);
        saveTime("start", startTime);
    }


    @Override
    public void onPause() {
        super.onPause();
        long endTime = System.currentTimeMillis();
        saveAndResetSession(endTime, numOfCardsStudied, numOfCardsMastered);
    }

    private void saveAndResetSession(long time, int numOfCardsStudied, int numOfCardsMastered) {
        long startTime = sharedPreferences.getLong("start", 0);
        if (startTime != 0) {
            long duration = time - startTime;
            long durationInSec = duration / 1000;
            LocalDate today = LocalDate.now();
            studySessionViewModel.getSessionByDate(today, existingSession -> {
                if (existingSession != null) {
                    long newDuration = durationInSec + existingSession.getDuration();
                    existingSession.setDuration(newDuration);
                    studySessionViewModel.update(existingSession);
                } else {
                    studySessionViewModel.insert(new StudySession(today, durationInSec));
                }
                sharedAnalyticsViewModel.updateAllAnalytics(numOfCardsStudied, numOfCardsMastered, easyCounter, mediumCounter, hardCounter);
                
                // Update Deck Performance
                
                sharedAnalyticsViewModel.updateDeckPerformance(sharedDeckAndCardViewModel.getDeckId().getValue(), numOfCardsStudied, easyCounter, mediumCounter, hardCounter);
                
                
                editor.remove("start");
                editor.remove("cardsStudied");
                editor.remove("cardsMastered");
                editor.remove("easy");
                editor.remove("medium");
                editor.remove("hard");
                editor.apply();
            });
        }
    }

    private void saveTime(String key, long time) {
        editor.putLong(key, time);
        editor.apply();
    }

    private void saveCardAnalytics(String key, int type) {
        editor.putInt(key, type);
        editor.apply();
    }
}

