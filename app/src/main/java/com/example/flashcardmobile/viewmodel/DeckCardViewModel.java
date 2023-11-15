package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.DeckCard;
import com.example.flashcardmobile.repository.DeckCardRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeckCardViewModel extends AndroidViewModel {
    private DeckCardRepository repository;
    private LiveData<List<DeckCard>> cards;


    public DeckCardViewModel(@NotNull Application application) {
        super(application);
        repository = new DeckCardRepository(application);
        cards = repository.getAllCards();
    }
    
    public LiveData<List<DeckCard>> getAllCards() {
        return cards;
    }
}
