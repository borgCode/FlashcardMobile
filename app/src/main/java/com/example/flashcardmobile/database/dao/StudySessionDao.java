package com.example.flashcardmobile.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import com.example.flashcardmobile.entity.StudySession;

@Dao
public interface StudySessionDao {

    @Insert
    void insert(StudySession studySession);
    @Update
    void update(StudySession studySession);
    @Delete
    void delete(StudySession studySession);
    
}
