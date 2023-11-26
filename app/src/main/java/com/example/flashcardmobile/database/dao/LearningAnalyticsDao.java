package com.example.flashcardmobile.database.dao;

import androidx.room.*;
import com.example.flashcardmobile.entity.LearningAnalytics;

import java.time.LocalDate;

@Dao
public interface LearningAnalyticsDao {
    
    @Insert
    void insert(LearningAnalytics learningAnalytics);
    @Update
    void update(LearningAnalytics learningAnalytics);
    @Delete
    void delete(LearningAnalytics learningAnalytics);
    @Query("SELECT * FROM learning_analytics WHERE date = :date")
    LearningAnalytics getAnalyticsByDate(LocalDate date);
}
