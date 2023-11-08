package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.repository.DeckRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {
    private DeckRepository deckRepository;
    private LiveData<List<Deck>> allDecks;
    public DeckViewModel(@NotNull Application application) {
        super(application);
        deckRepository = new DeckRepository(application);
        allDecks = deckRepository.getAllDecks();
    }
    
    public void insert(Deck deck) {
        deckRepository.insert(deck);
    }
    public void update(Deck deck) {
        deckRepository.update(deck);
    }
    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }
    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }
}
