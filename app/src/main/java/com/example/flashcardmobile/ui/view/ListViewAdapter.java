package com.example.flashcardmobile.ui.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    
    private List<DeckCard> cards;

    public ListViewAdapter(List<DeckCard> cards) {
        this.cards = cards;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deckNameCol;
        public TextView frontSideCol;
        public TextView backSideCol;
        public TextView dueDateCol;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            
            deckNameCol = itemView.findViewById(R.id.deckNameCol);
            frontSideCol = itemView.findViewById(R.id.frontCol);
            backSideCol = itemView.findViewById(R.id.backCol);
            dueDateCol = itemView.findViewById(R.id.dueDateCol);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table_cell_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListViewAdapter.ViewHolder holder, int position) {
        DeckCard card = cards.get(position);

        Log.d("ListView", "Setting column text: " +
                card.getFrontSide() + " " + card.getBackSide());
        holder.deckNameCol.setText(card.getDeckName());
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
