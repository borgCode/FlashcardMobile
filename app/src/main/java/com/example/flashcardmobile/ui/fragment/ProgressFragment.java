package com.example.flashcardmobile.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.LearningAnalytics;
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.viewmodel.SharedAnalyticsViewModel;
import com.example.flashcardmobile.viewmodel.StudySessionViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProgressFragment extends Fragment {
    private BarChart barChart;
    private PieChart pieChart;
    private StudySessionViewModel studySessionViewModel;
    private SharedAnalyticsViewModel sharedAnalyticsViewModel;
    private TabLayout yearTabLayout;
    private int selectedYear;
    private int selectedMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        studySessionViewModel = new ViewModelProvider(requireActivity()).get(StudySessionViewModel.class);
        sharedAnalyticsViewModel = new ViewModelProvider(requireActivity()).get(SharedAnalyticsViewModel.class);

        yearTabLayout = view.findViewById(R.id.years_tab_layout);
        yearTabLayout.addTab(yearTabLayout.newTab().setText("2023"));

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        selectedYear = currentYear;
        if (!yearTabExists(String.valueOf(currentYear))) {
            addYearTab(String.valueOf(currentYear));
        }

        yearTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedYear = Integer.parseInt(tab.getText().toString());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout monthTabLayout = view.findViewById(R.id.months_tab_layout);
        String[] months = getResources().getStringArray(R.array.months);
        for (String month : months) {
            monthTabLayout.addTab(monthTabLayout.newTab().setText(month));
        }
        monthTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedMonth = tab.getPosition() + 1;
                getDataForCharts(selectedYear, selectedMonth);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        barChart = view.findViewById(R.id.study_time_chart);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(10);
        barChart.setHighlightPerTapEnabled(false);
        
        pieChart = view.findViewById(R.id.progress_piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleAlpha(0);
        
        
        Legend pieLegend = pieChart.getLegend();
        pieLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        pieLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        pieLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        
        

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (yearTabLayout.getTabCount() > 0) {
                    yearTabLayout.getTabAt(yearTabLayout.getTabCount() - 1).select();
                }
                if (monthTabLayout.getTabCount() > currentMonth - 1) {
                    monthTabLayout.getTabAt(currentMonth - 1).select();
                }
            }
        }, 100);
        
        return view;
    }


    private void getDataForCharts(int selectedYear, int selectedMonth) {
        List<BarEntry> barEntries = new ArrayList<>();
        LocalDate[] monthLength = getMonthDateRange(selectedYear, selectedMonth);
        studySessionViewModel.getSessionsForMonth(monthLength[0], monthLength[1]).observe(getViewLifecycleOwner(), studySessions -> {
            barEntries.clear();
            for (StudySession session : studySessions) {
                LocalDate sessionDate = session.getSessionDate();
                long xValue = ChronoUnit.DAYS.between(monthLength[0], sessionDate);
                float yValue = session.getDuration();
                barEntries.add(new BarEntry(xValue, yValue));
            }
            initXAxisValueFormatter(monthLength[0]);

            populateBarChart(barEntries);

        });
        List<PieEntry> pieEntries = new ArrayList<>();
        
        sharedAnalyticsViewModel.getAnalyticsForMonth(monthLength[0], monthLength[1]).observe(getViewLifecycleOwner(), analytics -> {
            int cardsStudied = 0;
            int cardsAdded = 0;
            int cardsMastered = 0;
            pieEntries.clear();
            for (LearningAnalytics record: analytics) {
                cardsStudied += record.getCardsStudied();
                cardsAdded += record.getCardsAdded();
                cardsMastered += record.getCardsMastered();
            }
            pieEntries.add(new PieEntry(cardsStudied, "Cards studied this month"));
            pieEntries.add(new PieEntry(cardsAdded, "Cards added this month"));
            pieEntries.add(new PieEntry(cardsMastered, "Cards mastered this month"));
            populatePieChart(pieEntries);
        });
                
    }

    private void populateBarChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Time studied per day");
        dataSet.setValueFormatter(new ProgressFragment.DurationBarValueFormatter());
        BarData barData = new BarData(dataSet);
        barData.setValueTextSize(12f);
        barData.setBarWidth(0.9f);
        barData.setHighlightEnabled(false);
        barChart.setData(barData);
        barChart.invalidate();
    }
    
    private void populatePieChart(List<PieEntry> entries) {
        int[] pieChartColors = new int[]{Color.GREEN, Color.CYAN, Color.MAGENTA};
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(pieChartColors);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(11f);
        
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private LocalDate[] getMonthDateRange(int selectedYear, int selectedMonth) {
        LocalDate startOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(
                startOfMonth.getMonth().length(startOfMonth.isLeapYear()));
        return new LocalDate[]{startOfMonth, endOfMonth};
    }

    private boolean yearTabExists(String year) {
        for (int i = 0; i < yearTabLayout.getTabCount(); i++) {
            if (yearTabLayout.getTabAt(i) != null && yearTabLayout.getTabAt(i).getText().toString().equals(year)) {
                return true;
            }
        }
        return false;
    }

    private void addYearTab(String year) {
        yearTabLayout.addTab(yearTabLayout.newTab().setText(year));
    }

    private void initXAxisValueFormatter(LocalDate localDate) {
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                LocalDate date = localDate.plusDays((long) value);
                return date.format(DateTimeFormatter.ofPattern("dd"));
            }
        });
    }

    private class DurationBarValueFormatter extends ValueFormatter {
        public String getBarLabel(BarEntry barEntry) {
            int totalSeconds = (int) barEntry.getY();
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;

            if (hours > 0) {
                return String.format(Locale.ENGLISH, "%dh %dmin", hours, minutes);
            } else {
                return String.format(Locale.ENGLISH, "%dmin", minutes);
            }
        }
    }
    
    
}
