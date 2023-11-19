package com.example.flashcardmobile.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name="tag_name")
    private String tagName;

    public Tag(long id, String tagName) {
        this.tagName = tagName;
    }

    public long getId() {
        return id;
    }
    
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
