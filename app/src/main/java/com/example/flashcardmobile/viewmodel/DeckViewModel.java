package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Deck;
import com.example.flashcardmobile.entity.DeckWithInfo;
import com.example.flashcardmobile.repository.DeckRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class DeckViewModel extends AndroidViewModel {
    private final DeckRepository deckRepository;
    private final LiveData<List<Deck>> allDecks;
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
    public void deleteByDeckId(long id) {
        deckRepository.deleteDeckById(id);
    }
    public void deleteAllDecks() {
        deckRepository.deleteAllDecks();
    }
    public LiveData<Deck> getDeckById(long id) {
        return deckRepository.getDeckById(id);
    }
    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }
    public LiveData<List<DeckWithInfo>> getAllDecksWithInfo() {
        return deckRepository.getAllDecksWithInfo(LocalDateTime.now().toString());
    }

    public void updateDeckName(long deckId, String newName) {
        deckRepository.updateDeckName(deckId, newName);
    }
}
