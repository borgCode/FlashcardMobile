package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Badge;
import com.example.flashcardmobile.util.BadgeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BadgeViewModel extends AndroidViewModel {
    private final BadgeManager badgeManager;


    public BadgeViewModel(@NotNull Application application) {
        super(application);
        badgeManager = BadgeManager.getInstance(application);
    }
    
    public LiveData<List<Badge>> getAllBadges(LifecycleOwner lifecycleOwner) {
        return badgeManager.getAllBadges(lifecycleOwner);
    }
}
