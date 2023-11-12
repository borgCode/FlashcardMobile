package com.example.flashcardmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedPracticeViewModel extends ViewModel {
    private MutableLiveData<Long> deckId = new MutableLiveData<>();
    private MutableLiveData<String> deckName = new MutableLiveData<>();

    public void setDeckId(Long id) {
        deckId.setValue(id);
    }
    
    public LiveData<Long> getId() {
        return deckId;
    }

    public void setDeckName(String name) {
        deckName.setValue(name);
    }
    
    public LiveData<String> getName() {
        return deckName;
    }

    
}
