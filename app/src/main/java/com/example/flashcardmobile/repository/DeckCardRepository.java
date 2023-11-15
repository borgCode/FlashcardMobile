package com.example.flashcardmobile.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.database.AppDatabase;
import com.example.flashcardmobile.database.dao.DeckCardDao;
import com.example.flashcardmobile.entity.DeckCard;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DeckCardRepository {
    private DeckCardDao deckCardDao;
    
    public DeckCardRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        deckCardDao = database.deckCardDao();
    }
    
    public LiveData<List<DeckCard>> getAllCards() {
        return deckCardDao.getAllCards();
    }
}
