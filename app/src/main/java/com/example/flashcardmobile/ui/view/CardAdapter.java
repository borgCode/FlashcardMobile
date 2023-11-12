package com.example.flashcardmobile.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import com.example.flashcardmobile.viewmodel.CardViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Card> cards;
    
    public CardAdapter(List<Card> cards) {
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
        

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
