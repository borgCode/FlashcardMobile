package com.example.flashcardmobile.database;

import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromStringToDate(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }
    
    @TypeConverter
    public static String fromDateToString(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toString();
    }
    
}
