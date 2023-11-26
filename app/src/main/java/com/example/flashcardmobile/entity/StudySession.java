package com.example.flashcardmobile.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "study_sessions")
public class StudySession {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "session_date")
    private LocalDate sessionDate;
    private long duration;

    public StudySession(LocalDate sessionDate, long duration) {
        this.sessionDate = sessionDate;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
