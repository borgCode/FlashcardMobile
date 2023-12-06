package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.entity.DeckPerformance;
import com.example.flashcardmobile.entity.LearningAnalytics;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface AnalyticsDao {
    
    @Insert
    void insertLearningAnalytics(LearningAnalytics learningAnalytics);
    @Update
    void updateLearningAnalytics(LearningAnalytics learningAnalytics);
    @Delete
    void deleteLearningAnalytics(LearningAnalytics learningAnalytics);
    @Query("SELECT * FROM learning_analytics WHERE date = :date")
    LearningAnalytics getAnalyticsByDate(LocalDate date);
    @Query("SELECT * FROM learning_analytics WHERE date >= :startOfMonth AND date <= :endOfMonth")
    LiveData<List<LearningAnalytics>> getAnalyticsForMonth(LocalDate startOfMonth, LocalDate endOfMonth);
    
    @Insert
    void insertDeckPerformance(DeckPerformance deckPerformance);
    @Update
    void updateDeckPerformance(DeckPerformance deckPerformance);
    @Delete
    void deleteDeckPerformance(DeckPerformance deckPerformance);

    @Query("SELECT * FROM deck_performance WHERE deckId = :id")
    DeckPerformance getDeckPerformanceByDeckId(Long id);
}
