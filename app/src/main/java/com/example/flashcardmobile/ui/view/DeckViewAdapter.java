package com.example.flashcardmobile.ui.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Card;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class DeckViewAdapter extends RecyclerView.Adapter<DeckViewAdapter.ViewHolder> {

    public interface onCardOperationListener {
        void onCardEdit(long deckId, long cardId);
        void onResetDueDate(long cardId);
        void onCardDelete(long cardId);

    }
    
    private List<Card> cards;
    private onCardOperationListener listener;

    public DeckViewAdapter(List<Card> cards, onCardOperationListener listener) {
        this.listener = listener;
        this.cards = cards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView frontSideCol;
        public TextView backSideCol;
        public TextView dueDateCol;
        public ImageButton cardOptions;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            
            frontSideCol = itemView.findViewById(R.id.frontCol);
            backSideCol = itemView.findViewById(R.id.backCol);
            dueDateCol = itemView.findViewById(R.id.dueDateCol);
            cardOptions = itemView.findViewById(R.id.list_card_popup_menu);
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

        holder.cardOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.cardOptions);
            popupMenu.getMenuInflater().inflate(R.menu.card_list_popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                Log.d("PopupMenu", "item Clicked");
                int id = item.getItemId();
                if (id == R.id.editCardItem) {
                    listener.onCardEdit(card.getDeckId(), card.getId());
                } else if (id == R.id.resetDueDateItem) {
                    listener.onResetDueDate(card.getId());
                } else if (id == R.id.deleteCardItem) {
                    listener.onCardDelete(card.getId());
                } else {
                    return false;
                }
                return true;
            });
            Log.d("PopupMenu", "Showing menu");
            popupMenu.show();

        });

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
