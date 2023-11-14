package com.example.flashcardmobile.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class DeckViewAdapter extends RecyclerView.Adapter<DeckViewAdapter.ViewHolder> {
    
    private List<Card> cards;

    public DeckViewAdapter(List<Card> cards) {
        this.cards = cards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView frontSideCol;
        public TextView backSideCol;
        public TextView dueDateCol;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            
            frontSideCol = itemView.findViewById(R.id.frontCol);
            backSideCol = itemView.findViewById(R.id.backCol);
            dueDateCol = itemView.findViewById(R.id.dueDateCol);
        }
    }
    @NonNull
    @NotNull
    @Override
    public DeckViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table_cell_view_deck, parent, false);
        
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeckViewAdapter.ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.frontSideCol.setText(card.getFrontSide());
        holder.backSideCol.setText(card.getBackSide());
        
        LocalDate date = card.getDueDate().toLocalDate();
        holder.dueDateCol.setText(date.toString());

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
