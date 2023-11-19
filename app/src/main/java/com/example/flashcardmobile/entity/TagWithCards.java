package com.example.flashcardmobile.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class TagWithCards {
    @Embedded
    public Tag tag;
    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = CardTagCrossRef.class,
                    parentColumn = "tagId",
                    entityColumn = "cardId")
    )
    public List<Card> cards;
}
