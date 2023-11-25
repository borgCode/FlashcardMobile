package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.viewmodel.StudySessionViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(10);
        barChart.setHighlightPerTapEnabled(false);
        

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
    }


    private void getDataForChart(int selectedYear, int selectedMonth) {
        List<BarEntry> entries = new ArrayList<>();
        LocalDate[] monthLength = getMonthDateRange(selectedYear, selectedMonth);
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

    private void populateChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Time studied per day");
        dataSet.setValueFormatter(new DurationBarValueFormatter());
        BarData barData = new BarData(dataSet);
        barData.setValueTextSize(12f);
        barData.setBarWidth(0.9f);
        barData.setHighlightEnabled(false);
        barChart.setData(barData);
        barChart.invalidate();
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
