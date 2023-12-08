package com.example.flashcardmobile.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.flashcardmobile.database.dao.*;
import com.example.flashcardmobile.entity.*;

@Database(entities = {Deck.class, Card.class, Tag.class, CardTagCrossRef.class,
        StudySession.class, LearningAnalytics.class, DeckPerformance.class, Badge.class}, version = 14)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();
    public abstract DeckCardDao deckCardDao();
    public abstract TagDao tagDao();
    public abstract StudySessionDao studySessionDao();
    public abstract AnalyticsDao learningAnalyticsDao();
    public abstract BadgeDao badgeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database.db")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
