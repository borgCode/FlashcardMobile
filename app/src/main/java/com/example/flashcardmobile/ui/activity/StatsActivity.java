package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
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
    private LocalDate firstDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        studySessionViewModel = new ViewModelProvider(this).get(StudySessionViewModel.class);

        setContentView(R.layout.activity_stats);
        
        TabLayout tabLayout = findViewById(R.id.months_tab_layout);
        String[] months = getResources().getStringArray(R.array.months);
        for (String month: months) {
            tabLayout.addTab(tabLayout.newTab().setText(month));
        }
        
        
        barChart = findViewById(R.id.study_time_chart);
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            LocalDate date = firstDate.plusDays((long) value);
            return date.format(DateTimeFormatter.ofPattern("dd"));
        });
        getDataForChart();


    }

    private void getDataForChart() {
        List<BarEntry> entries = new ArrayList<>();
        studySessionViewModel.getFirstDateValue().observe(this, date -> {
            firstDate = date;
            studySessionViewModel.getAllSessions().observe(this, studySessions -> {
                for (StudySession session : studySessions) {
                    LocalDate sessionDate = session.getSessionDate();
                    long xValue = ChronoUnit.DAYS.between(firstDate, sessionDate);
                    float yValue = session.getDuration();
                    entries.add(new BarEntry(xValue, yValue));
                }
                populateChart(entries);
            });
        });

    }

    private void populateChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Session Data");
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.3f);
        barChart.setData(barData);
        barChart.invalidate();
    }
}
