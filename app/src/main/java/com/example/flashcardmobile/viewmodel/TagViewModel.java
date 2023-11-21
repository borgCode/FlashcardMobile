package com.example.flashcardmobile.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.CardTagCrossRef;
import com.example.flashcardmobile.entity.CardWithTags;
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.repository.TagRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagViewModel extends AndroidViewModel {
    private TagRepository tagRepository;
    private LiveData<List<Tag>> tags;
    
    public TagViewModel(@NotNull Application application) {
        super(application);
        tagRepository = new TagRepository(application);
    }

    public void insert(Tag tag) {
        tagRepository.insert(tag);
    }

    public void update(Tag tag) {
        tagRepository.update(tag);
    }

    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }
    
    public LiveData<Tag> getTagById(long id) {
        return tagRepository.getTagById(id);
    }
    
    public LiveData<List<Tag>> getAllTags() {
        return tagRepository.getAllTags();
    }

    public void insertCrossRefs(List<CardTagCrossRef> crossRefs) {
        tagRepository.insertCrossRefs(crossRefs);
    }
    public void updateCrossRefs(long cardId, List<CardTagCrossRef> crossRefs) {
        tagRepository.updateCrossRefs(cardId, crossRefs);
    }
    public LiveData<CardWithTags> getCardWithTags(long cardId) {
        return tagRepository.getTagsForCard(cardId);
    }
}