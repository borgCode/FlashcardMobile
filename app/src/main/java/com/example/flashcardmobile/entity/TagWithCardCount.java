package com.example.flashcardmobile.entity;

import androidx.room.Embedded;

public class TagWithCardCount {
    @Embedded
    private Tag tag;
    private int cardCount;

    public TagWithCardCount(Tag tag, int cardCount) {
        this.tag = tag;
        this.cardCount = cardCount;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }
}
