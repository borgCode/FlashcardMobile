package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.flashcardmobile.entity.StudySession;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface StudySessionDao {

    @Insert
    void insert(StudySession studySession);
    @Update
    void update(StudySession studySession);
    @Delete
    void delete(StudySession studySession);
    @Query("SELECT COUNT (*) FROM study_sessions")
    int getSessionCount();
    @Query("SELECT session_date FROM study_sessions LIMIT 1")
    LiveData<LocalDate> getFirstValue();

    @Query("SELECT * FROM study_sessions WHERE session_date >= :startOfMonth AND session_date <= :endOfMonth")
    LiveData<List<StudySession>> getSessionsForMonth(LocalDate startOfMonth, LocalDate endOfMonth);

    @Query("SELECT * FROM study_sessions WHERE session_date = :today")
    StudySession getSessionByDate(LocalDate today);
    
    @Query("SELECT COUNT(DISTINCT DATE(session_date)) FROM study_sessions WHERE session_date BETWEEN :startDate AND :endDate;")
    LiveData<Integer> countUniqueStudyDays(LocalDate startDate, LocalDate endDate);
}
