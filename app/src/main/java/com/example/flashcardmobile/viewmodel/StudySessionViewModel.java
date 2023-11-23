package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.StudySession;
import com.example.flashcardmobile.repository.StudySessionRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class StudySessionViewModel extends AndroidViewModel {
    private StudySessionRepository studySessionRepository;
    private LiveData<List<StudySession>> sessions;


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
    public LiveData<List<StudySession>> getAllSessions() {
        return studySessionRepository.getAllSessions();
    }
    public LiveData<LocalDate> getFirstDateValue() {
        return studySessionRepository.getFirstDateValue();
    }
}
