package com.example.flashcardmobile.repository;

import android.util.Log;
import com.example.flashcardmobile.database.dao.StudySessionDao;
import com.example.flashcardmobile.entity.StudySession;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class StudySessionRepository {
    
    private StudySessionDao studySessionDao;
    private ExecutorService executorService;

    public void insert(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.insert(studySession), executorService);
    }

    public void update(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.update(studySession), executorService);
    }

    public void delete(StudySession studySession) {
        CompletableFuture.runAsync(() -> studySessionDao.delete(studySession), executorService);
    }
}
