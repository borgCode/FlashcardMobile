package com.example.flashcardmobile.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    public interface AdapterCallback {
        void onDifficultySelected(Card card, int difficulty);
    }

    private List<Card> cards;
    private AdapterCallback callback;

    public CardAdapter(List<Card> cards, AdapterCallback callback) {
        this.callback = callback;
        this.cards = cards;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView frontSide;
        public TextView backSide;
        public Button easy;
        public Button medium;
        public Button hard;

        public CardViewHolder(View itemView) {
            super(itemView);
            frontSide = itemView.findViewById(R.id.frontSide);
            backSide = itemView.findViewById(R.id.backSide);
            easy = itemView.findViewById(R.id.buttonEasy);
            medium = itemView.findViewById(R.id.buttonMedium);
            hard = itemView.findViewById(R.id.buttonHard);

        }

    }

    @NonNull
    @NotNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager2_item_flashcard, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardAdapter.CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.frontSide.setText(card.getFrontSide());
        holder.backSide.setText(card.getBackSide());

        DifficultyButtonClickListener listener = new DifficultyButtonClickListener(card);
        holder.easy.setOnClickListener(listener);
        holder.medium.setOnClickListener(listener);
        holder.hard.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private class DifficultyButtonClickListener implements View.OnClickListener {
        private Card card;

        public DifficultyButtonClickListener(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View v) {
            int buttonId = v.getId();
            int difficulty;
            if (buttonId == R.id.buttonEasy) {
                difficulty = 1;
            } else if (buttonId == R.id.buttonMedium) {
                difficulty = 3;

            } else if (buttonId == R.id.buttonHard) {
                difficulty = 5;
            } else {
                return;
            }
            callback.onDifficultySelected(card, difficulty);
        }
    }
}


