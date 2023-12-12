package com.example.flashcardmobile.entity;

import androidx.room.Entity;

@Entity(tableName = "card_tag_cross_ref",
        primaryKeys = {"cardId", "tagId"})
        
public class CardTagCrossRef {
        private long cardId;
        private long tagId;

        public CardTagCrossRef(long cardId, long tagId) {
                this.cardId = cardId;
                this.tagId = tagId;
        }

        public long getCardId() {
                return cardId;
        }

        public void setCardId(long cardId) {
                this.cardId = cardId;
        }

        public long getTagId() {
                return tagId;
        }

        public void setTagId(long tagId) {
                this.tagId = tagId;
        }
}
