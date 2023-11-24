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
    @Query("SELECT * FROM study_sessions")
    LiveData<List<StudySession>> getAllSessions();
    @Query("SELECT sessionDate FROM study_sessions LIMIT 1")
    LiveData<LocalDate> getFirstValue();

    @Query("SELECT * FROM study_sessions WHERE sessionDate >= :startOfMonth AND sessionDate <= :endOfMonth")
    LiveData<List<StudySession>> getSessionsForMonth(LocalDate startOfMonth, LocalDate endOfMonth);

    @Query("SELECT * FROM study_sessions WHERE sessionDate = :today")
    StudySession getSessionByDate(LocalDate today);
}
