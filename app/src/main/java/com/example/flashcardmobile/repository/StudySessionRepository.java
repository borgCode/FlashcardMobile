package com.example.flashcardmobile.repository;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.StudySessionDao;
import com.example.flashcardmobile.entity.StudySession;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class StudySessionRepository {
    
    private StudySessionDao studySessionDao;
    private ExecutorService executorService;

    public StudySessionRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        studySessionDao = database.studySessionDao();
    }

    public void insert(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.insert(studySession), executorService);
    }

    public void update(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.update(studySession), executorService);
    }

    public void delete(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.delete(studySession), executorService);
    }
    public LiveData<List<StudySession>> getAllSessions() {
        return studySessionDao.getAllSessions();
    }
    
    public LiveData<LocalDate> getFirstDateValue() {
        return studySessionDao.getFirstValue();
    }
}
