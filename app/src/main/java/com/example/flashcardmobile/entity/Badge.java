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
    private String tag;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "achieved")
    private boolean achieved;
    @ColumnInfo(name = "achievement_criteria")
    private String achievementCriteria;
    private int level;

    public Badge(String name, String tag, String description, boolean achieved, String achievementCriteria, int level) {
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.achieved = achieved;
        this.achievementCriteria = achievementCriteria;
        this.level = level;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
