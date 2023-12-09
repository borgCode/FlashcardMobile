package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.repository.StudySessionRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class StudySessionViewModel extends AndroidViewModel {
    private StudySessionRepository studySessionRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    


    public StudySessionViewModel(@NotNull Application application) {
        super(application);
        studySessionRepository = new StudySessionRepository(application);

    }
    public void insert(StudySession studySession) {
        studySessionRepository.insert(studySession);
    }

    public void update(StudySession studySession) {
        studySessionRepository.update(studySession);
    }
    public void delete(StudySession studySession) {
        studySessionRepository.delete(studySession);
    }
    
    public LiveData<LocalDate> getFirstDateValue() {
        return studySessionRepository.getFirstDateValue();
    }
    public void getSessionByDate(LocalDate date, Consumer<StudySession> onResult) {
        CompletableFuture<StudySession> futureSession = studySessionRepository.getSessionByDate(date);
        futureSession.thenAcceptAsync(onResult, executorService);
    }

    public LiveData<List<StudySession>> getSessionsForMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return studySessionRepository.getSessionsForMonth(startOfMonth, endOfMonth);
    }
}
