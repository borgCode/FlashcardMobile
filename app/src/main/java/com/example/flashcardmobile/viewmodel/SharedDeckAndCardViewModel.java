package com.example.flashcardmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.flashcardmobile.entity.Card;

public class SharedDeckAndCardViewModel extends ViewModel {
    private final MutableLiveData<Long> deckId = new MutableLiveData<>();
    private final MutableLiveData<Card> selectedCard = new MutableLiveData<>();

    public void setDeckId(Long id) {
        deckId.setValue(id);
    }
    
    public LiveData<Long> getDeckId() {
        return deckId;
    }
    
    public void setSelectedCard(Card card) {
        selectedCard.setValue(card);
    }
    
    public LiveData<Card> getSelectedCard() {
        return selectedCard;
    }

    
}
