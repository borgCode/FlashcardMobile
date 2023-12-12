package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.repository.CardRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CardViewModel extends AndroidViewModel {
    private final CardRepository cardRepository;
    private LiveData<List<Card>> cards;


    public CardViewModel(@NotNull Application application) {
        super(application);
        cardRepository = new CardRepository(application);
        
    }

    public CompletableFuture<Long> insert(Card card) {
        
        return cardRepository.insert(card);
    }
    public void update(Card card) {
        cardRepository.update(card);
    }
    public void delete(Card card) {
        cardRepository.delete(card);
    }
    public void deleteCardById(long id) {
        cardRepository.deleteById(id);
    }
    
    public LiveData<List<Card>> getDueCards(long deckId) {
        cards = cardRepository.getAllDueCards(deckId);
        return cards;
    }
    public LiveData<Card> getCardById(long id) {
        return cardRepository.getCardById(id);
    }
    
    public LiveData<List<Card>> getAllDeckCards(long deckId) {
        cards = cardRepository.getAllDeckCards(deckId);
        return cards;
    }
}
