package com.example.flashcardmobile.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.flashcardmobile.entity.Badge;

import java.util.List;

@Dao
public interface BadgeDao {
    @Insert
    void insert (Badge badge);
    @Update
    void update (Badge badge);

    @Query("SELECT COUNT(*) FROM badges")
    int countBadges();

    @Query("SELECT * FROM badges ORDER BY id")
    LiveData<List<Badge>> getAllBadges();
}
