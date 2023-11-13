package com.example.flashcardmobile.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.flashcardmobile.database.dao.CardDao;
import com.example.flashcardmobile.database.dao.DeckDao;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.entity.Deck;

@Database(entities = {Deck.class, Card.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database.db")
                    .fallbackToDestructiveMigration().build();
            
            
        }
        return instance;
    }
}
