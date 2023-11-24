package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.viewmodel.StudySessionViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;
    private StudySessionViewModel studySessionViewModel;
    private TabLayout yearTabLayout;
    private int selectedYear;
    private int selectedMonth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        studySessionViewModel = new ViewModelProvider(this).get(StudySessionViewModel.class);

        setContentView(R.layout.activity_stats);

        
        
        yearTabLayout = findViewById(R.id.years_tab_layout);
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
                Log.d("Year tab ", "Selected year: " + selectedYear);
                
                
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        
        TabLayout monthTabLayout = findViewById(R.id.months_tab_layout);
        String[] months = getResources().getStringArray(R.array.months);
        for (String month : months) {
            monthTabLayout.addTab(monthTabLayout.newTab().setText(month));
        }
        monthTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedMonth = tab.getPosition() + 1;
                Log.d("Month tab selection", "Selected month: " + selectedMonth);
                Log.d("Month tab selection", "Selected year: " + selectedYear);
                getDataForChart(selectedYear, selectedMonth);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        barChart = findViewById(R.id.study_time_chart);
        getDataForChart(currentYear, currentMonth);
    }
    

    private void getDataForChart(int selectedYear, int selectedMonth) {
        List<BarEntry> entries = new ArrayList<>();
        LocalDate[] monthLength = getMonthDateRange(selectedYear, selectedMonth);
        Log.d("Get Data from DB", "Month length: " + monthLength[0] + " + " + monthLength[1]);
        studySessionViewModel.getSessionsForMonth(monthLength[0], monthLength[1]).observe(this, studySessions -> {
            entries.clear();
            for (StudySession session : studySessions) {
                LocalDate sessionDate = session.getSessionDate();
                long xValue = ChronoUnit.DAYS.between(monthLength[0], sessionDate);
                float yValue = session.getDuration();
                entries.add(new BarEntry(xValue, yValue));
            }
            initXAxisValueFormatter(monthLength[0]);
            populateChart(entries);

        });

    }

    private void initXAxisValueFormatter(LocalDate localDate) {
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            LocalDate date = localDate.plusDays((long) value);
            return date.format(DateTimeFormatter.ofPattern("dd"));
        });
    }

    private LocalDate[] getMonthDateRange(int selectedYear, int selectedMonth) {
        LocalDate startOfMonth = LocalDate.of(selectedYear, selectedMonth, 1);
        Log.d("Get Month Range", "Start of month: " + startOfMonth);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(
                startOfMonth.getMonth().length(startOfMonth.isLeapYear()));
        Log.d("Get Month Range", "End of month: " + endOfMonth);
        return new LocalDate[]{startOfMonth, endOfMonth};
    }

    private boolean yearTabExists(String year) {
        Log.d("Check if year tab exists", "Checking if tab exists");
        for (int i = 0; i < yearTabLayout.getTabCount(); i++) {
            Log.d("Check if year tab exists", "Tab child count: " + yearTabLayout.getTabCount()
                    + "\nExisting year tab is: " + yearTabLayout.getTabAt(0).getText().toString());
            if (yearTabLayout.getTabAt(i) != null && yearTabLayout.getTabAt(i).getText().toString().equals(year)) {
                return true;
            }
        }
        return false;
    }

    private void addYearTab(String year) {
        Log.d("Add year tab method", "Adding tab and setting text to: " + year);
        yearTabLayout.addTab(yearTabLayout.newTab().setText(year));
    }

    private void populateChart(List<BarEntry> entries) {
        Log.d("Populate chart", "Populating chart");
        BarDataSet dataSet = new BarDataSet(entries, "Session Data");
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.3f);
        Log.d("Populate chart", "Bar width set");
        barChart.setData(barData);
        Log.d("Populate chart", "Invalidating");
        barChart.invalidate();
    }


}
