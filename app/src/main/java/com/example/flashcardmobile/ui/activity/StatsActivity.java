package com.example.flashcardmobile.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flashcardmobile.R;
import com.github.mikephil.charting.charts.BarChart;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_stats);
        barChart = findViewById(R.id.study_time_chart);
        populateChart();
        
        
    }

    private void populateChart() {
        
    }
}
