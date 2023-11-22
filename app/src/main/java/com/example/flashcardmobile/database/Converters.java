package com.example.flashcardmobile.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromStringToDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }
    
    @TypeConverter
    public static String fromDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toString();
    }

    @TypeConverter
    public static LocalDate fromStringToDate(String value) {
        return value == null ? null : LocalDate.parse(value);
    }

    @TypeConverter
    public static String fromDateToString(LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }
    
}
