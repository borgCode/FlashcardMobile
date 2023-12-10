package com.example.flashcardmobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.ui.dialog.CustomizeStreaksDialog;
import com.example.flashcardmobile.viewmodel.DeckViewModel;
import com.example.flashcardmobile.viewmodel.SharedAnalyticsViewModel;
import com.example.flashcardmobile.viewmodel.StudySessionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class PerformanceFragment extends Fragment implements CustomizeStreaksDialog.OnButtonSelectedListener {

    private DeckViewModel deckViewModel;
    private SharedAnalyticsViewModel sharedAnalyticsViewModel;
    private AutoCompleteTextView deckSelection;
    private ArrayAdapter<String> adapter;
    private Map<String, Long> deckMap = new HashMap<>();
    private PieChart pieChart;
    private TextView streakTitle;
    private TextView streakDisplay;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        sharedPreferences = getActivity().getSharedPreferences("AppSettings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        sharedAnalyticsViewModel = new ViewModelProvider(requireActivity()).get(SharedAnalyticsViewModel.class);

        deckSelection = view.findViewById(R.id.performance_deck_selection);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        deckSelection.setAdapter(adapter);

        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            deckMap = decks.stream().collect(Collectors.toMap(Deck::getDeckName, Deck::getId, (existing, replacement) -> existing, HashMap::new));
            adapter.clear();
            adapter.addAll(new ArrayList<>(deckMap.keySet()));
        });

        deckSelection.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (deckSelection.getRight() - deckSelection.getCompoundDrawables()[2].getBounds().width())) {
                    deckSelection.showDropDown();
                    return true;
                }
            }
            return false;
        });
        
        streakTitle = view.findViewById(R.id.performance_streak_title);
        
        deckSelection.setOnItemClickListener((((parent, view1, position, id) -> {
            String selectedDeckName = (String) parent.getItemAtPosition(position);
            long deckId = deckMap.get(selectedDeckName);
            loadPieChartData(deckId);
            streakTitle.setText("Study streak for " + selectedDeckName);
        })));
        
        pieChart = view.findViewById(R.id.performance_piechart);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.getDescription().setTextSize(16f);
        pieChart.setNoDataText("Select a deck to display data");

        Legend pieLegend = pieChart.getLegend();
        pieLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        pieLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        pieLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        
        
        Button streaksButton = view.findViewById(R.id.customize_streaks);
        streaksButton.setOnClickListener(v -> {
            CustomizeStreaksDialog dialog = new CustomizeStreaksDialog();
            dialog.setButtonSelectedListener(this);
            dialog.show(getParentFragmentManager(), "streaksDialog");
        });
        
        streakDisplay = view.findViewById(R.id.streaks_display);
                
        SwitchCompat streaksBtn = view.findViewById(R.id.streaks_toggle_button);
        if (streaksBtn.isChecked()) {
            getCurrentUserStreak();
        }



        

        return view;
    }
    

    private void loadPieChartData(long deckId) {
        List<PieEntry> pieEntries = new ArrayList<>();
        sharedAnalyticsViewModel.getDeckPerformanceByDeckId(deckId).thenAccept(deckPerformance -> {
            if (deckPerformance != null) {
                pieEntries.clear();
                pieEntries.add(new PieEntry(deckPerformance.getEasyAnswers(), "Cards selected as easy"));
                pieEntries.add(new PieEntry(deckPerformance.getMediumAnswers(), "Cards selected as medium"));
                pieEntries.add(new PieEntry(deckPerformance.getHardAnswers(), "Cards selected as hard"));
                pieChart.getDescription().setText("Total cards studied: " + deckPerformance.getCardsStudied());
                populatePieChart(pieEntries);
            }
        });
    }

    private void populatePieChart(List<PieEntry> pieEntries) {
        int greenColor = Color.parseColor("#298b28");
        int yellowColor = Color.parseColor("#8E790C");
        int redColor = Color.parseColor("#B22222");
        int[] pieChartColors = new int[]{greenColor, yellowColor, redColor};
        
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(pieChartColors);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(11f);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void getCurrentUserStreak() {
        String streakPref = sharedPreferences.getString("streak", "daily");
        if (streakPref.equals("daily")) {
            String streakText = "Your current daily streak is: " + sharedPreferences.getInt("currentDailyStreak", 0);
            streakDisplay.setText(streakText);
        } else if (streakPref.equals("weekly")) {
            String streakText = "Your current weekly streak is: " + sharedPreferences.getInt("currentWeeklyStreak", 0);
            streakDisplay.setText(streakText);
        }
    }

    @Override
    public void onDailyStreakSelected() {
        editor.putString("streak", "daily");
        editor.apply();
        getCurrentUserStreak();
    }

    @Override
    public void onWeeklyStreakSelected(int selectedDays) {
        editor.putString("streak", "weekly");
        editor.putInt("numOfDays", selectedDays);
        editor.apply();
        getCurrentUserStreak();
    }
}
