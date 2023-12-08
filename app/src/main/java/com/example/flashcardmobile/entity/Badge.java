package com.example.flashcardmobile.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "badges")
public class Badge {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "achieved")
    private boolean achieved;
    @ColumnInfo(name = "achievement_criteria")
    private String achievementCriteria;

    public Badge(String name, String description, boolean achieved, String achievementCriteria) {
        this.name = name;
        this.description = description;
        this.achieved = achieved;
        this.achievementCriteria = achievementCriteria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public String getAchievementCriteria() {
        return achievementCriteria;
    }

    public void setAchievementCriteria(String achievementCriteria) {
        this.achievementCriteria = achievementCriteria;
    }
}
