package com.example.flashcardmobile.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckWithInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeckListViewAdapter extends RecyclerView.Adapter<DeckListViewAdapter.ViewHolder> {
    
    public interface onDeckOperationListener {
        void onEditDeckName(long deckId, String deckName);
        void onViewAllCards(long deckId);
        void onViewDueCards(long deckId);
    }
    
    private List<DeckWithInfo> decks;
    private onDeckOperationListener listener;
    
    public DeckListViewAdapter(onDeckOperationListener listener) {
        decks = new ArrayList<>();
        this.listener = listener;
        
    }
    
    public void setDecks(ArrayList<DeckWithInfo> decks) {
        this.decks = decks;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        public TextView deckNameCol;
        public TextView deckSizeCol;
        public TextView dueCardSize;
        public ImageButton deckOptions;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            
            deckNameCol = itemView.findViewById(R.id.deck_name_col);
            deckSizeCol = itemView.findViewById(R.id.deck_size_col);
            dueCardSize = itemView.findViewById(R.id.due_card_col);
            deckOptions = itemView.findViewById(R.id.list_deck_popup_menu);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck_table_list_cell_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        
        DeckWithInfo deck = decks.get(position);
        
        holder.deckNameCol.setText(deck.getDeck().getDeckName());
        holder.deckSizeCol.setText(String.valueOf(deck.getDeckSize()));
        holder.dueCardSize.setText(String.valueOf(deck.getDueCardSize()));
        
        holder.deckOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.deckOptions);
            popupMenu.getMenuInflater().inflate(R.menu.deck_list_popup_menu, popupMenu.getMenu());
            
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.edit_deck_name_item) {
                    listener.onEditDeckName(deck.getDeck().getId(), deck.getDeck().getDeckName());
                } else if (id == R.id.view_all_cards_item) {
                    //TODO same approach as tag tab?
                } else if (id == R.id.view_due_cards_item) {
                    //TODO -||-
                } else {
                    return false;
                }
                return true;
            });
            
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return decks.size();
    }
    
    
}
