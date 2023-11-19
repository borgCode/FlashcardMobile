package com.example.flashcardmobile.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CardWithTags {
    @Embedded
    public Card card;
    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = CardTagCrossRef.class,
                    parentColumn = "cardId",
                    entityColumn = "tagId")
    )
    public List<Tag> tags;
}
