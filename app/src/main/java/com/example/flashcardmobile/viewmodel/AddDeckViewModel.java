package com.example.flashcardmobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDeckViewModel extends ViewModel {
    private MutableLiveData<CharSequence> name = new MutableLiveData<>();
    
    public void setName(CharSequence input) {
        name.setValue(input);
    }
    
    public LiveData<CharSequence> getName() {
        return name;
    }
}
