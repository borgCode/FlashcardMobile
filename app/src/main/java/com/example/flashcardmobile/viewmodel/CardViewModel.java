package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.repository.CardRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CardViewModel extends AndroidViewModel {
    private CardRepository cardRepository;
    private LiveData<List<Card>> cards;


    public CardViewModel(@NotNull Application application) {
        super(application);
        cardRepository = new CardRepository(application);
        
    }

    public void insert(Card card) {
        cardRepository.insert(card);
    }
    public void update(Card card) {
        cardRepository.update(card);
    }
    public void delete(Card card) {
        cardRepository.delete(card);
    }
    
    public LiveData<List<Card>> getDueCards(long deckId) {
        cards = cardRepository.getAllDueCards(deckId);
        return cards;
    } 
}
