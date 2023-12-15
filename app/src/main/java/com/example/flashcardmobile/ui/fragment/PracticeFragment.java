package com.example.flashcardmobile.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
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
import com.example.flashcardmobile.ui.dialog.DeleteConfirmationDialog;
import com.example.flashcardmobile.ui.view.CardAdapter;
import com.example.flashcardmobile.viewmodel.*;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment implements CardAdapter.AdapterCallback, DeleteConfirmationDialog.DeleteDialogListener{
    private CardViewModel cardViewModel;
    private SharedDeckAndCardViewModel sharedDeckAndCardViewModel;
    private DeckViewModel deckViewModel;
    private StudySessionViewModel studySessionViewModel;
    private SharedAnalyticsViewModel sharedAnalyticsViewModel;
    private List<Card> cards;
    private CardAdapter cardAdapter;
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

        ViewPager2 viewPager2 = view.findViewById(R.id.practiceView);
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
        
        Button finishedStudyingBtn = view.findViewById(R.id.finished_studying_button);
        finishedStudyingBtn.setOnClickListener(v -> {
            AddCardFragment addCardFragment = new AddCardFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, addCardFragment)
                    .addToBackStack(null)
                    .commit();
        });

        sharedDeckAndCardViewModel.getDeckId().observe(getViewLifecycleOwner(), deckId -> {
            cardViewModel.getDueCards(deckId).observe(getViewLifecycleOwner(), newCards -> {
                cards.clear();
                cards.addAll(newCards);
                cardAdapter.notifyDataSetChanged();
                if (newCards.isEmpty()) {
                    viewPager2.setVisibility(View.GONE);
                    view.findViewById(R.id.finished_studying_text).setVisibility(View.VISIBLE);
                    finishedStudyingBtn.setVisibility(View.VISIBLE);
                    
                } else {
                    viewPager2.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.finished_studying_text).setVisibility(View.GONE);
                    finishedStudyingBtn.setVisibility(View.GONE);
                }
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
                } else if (id == R.id.deleteCardItem) {
                    showDeleteDialog("Do you really want to delete this card?", "single_card");
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        return view;
    }

    private void showDeleteDialog(String dialogMessage, String type) {
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(dialogMessage, type);
        dialog.setDeleteDialogListener(this);
        dialog.show(getParentFragmentManager(), "deleteDialog");
    }

    @Override
    public void onConfirmDelete(String confirmationType) {
        cardViewModel.delete(cards.get(currentCardPosition));
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
                
                //Update streak
                
                LocalDate lastSessionDate = LocalDate.parse(sharedPreferences.getString("lastSession", LocalDate.now().toString()));
                if (lastSessionDate.equals(today.minusDays(1))) {
                    int currentDailyStreak = sharedPreferences.getInt("currentDailyStreak", 0);
                    editor.putInt("currentDailyStreak", currentDailyStreak + 1);
                } else if (!lastSessionDate.equals(today)) {
                    editor.putInt("currentDailyStreak", 1);
                }
                
                LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                
                studySessionViewModel.getUniqueStudyDays(startOfWeek, endOfWeek).observe(getViewLifecycleOwner(), count -> {
                    if (count >= sharedPreferences.getInt("numOfDays", 0)) {
                        int currentWeeklyStreak = sharedPreferences.getInt("currentWeeklyStreak", 0);
                        editor.putInt("currentWeeklyStreak", currentWeeklyStreak +1);
                    } else {
                        editor.putInt("currentWeeklyStreak", 1);
                    }
                });
                
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

