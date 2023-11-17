package com.example.flashcardmobile.ui.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> implements Filterable {
    
    public interface onCardOperationListener {
        void onCardEdit(long deckId, long cardId);
        void onResetDueDate(int position);
        void onCardDelete(long cardId);
        
    }

    private List<DeckCard> cards;
    private List<DeckCard> cardsFull;
    private onCardOperationListener listener;

    public ListViewAdapter(onCardOperationListener listener) {
        this.listener = listener;
        cards = new ArrayList<>();
        cardsFull = new ArrayList<>();
    }

    public void setCards(ArrayList<DeckCard> cards) {
        this.cards = cards;
    }

    public void setCardsFull(ArrayList<DeckCard> cards) {
        this.cardsFull = cards;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deckNameCol;
        public TextView frontSideCol;
        public TextView backSideCol;
        public TextView dueDateCol;
        public ImageButton cardOptions;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            deckNameCol = itemView.findViewById(R.id.deckNameCol);
            frontSideCol = itemView.findViewById(R.id.frontCol);
            backSideCol = itemView.findViewById(R.id.backCol);
            dueDateCol = itemView.findViewById(R.id.dueDateCol);
            cardOptions = itemView.findViewById(R.id.list_card_popup_menu);
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
        
        holder.cardOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.cardOptions);
            popupMenu.getMenuInflater().inflate(R.menu.list_popup_menu, popupMenu.getMenu());
            
            popupMenu.setOnMenuItemClickListener(item -> {
                Log.d("PopupMenu", "item Clicked");
                int id = item.getItemId();
                if (id == R.id.editCardItem) {
                    listener.onCardEdit(card.getDeckId(), card.getCardId());
                } else if (id == R.id.resetDueDateItem) {
                    listener.onResetDueDate(position);
                } else if (id == R.id.deleteCardItem) {
                    listener.onCardDelete(card.getCardId());
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

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("ListViewAdapter", "Checking list size: " +
                    "\n cards: " + cards.size() +
                    "\n cardsFull: " + cardsFull.size());
            List<DeckCard> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                Log.d("Search Empty", "Search is empty, adding all cards to list");
                filteredList.addAll(cardsFull);
                Log.d("Search Empty", "All Cards added");
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d("Filter pattern", filterPattern);
                
                for (DeckCard card : cardsFull) {
                    if (card.getFrontSide().toLowerCase().contains(filterPattern)
                    || card.getBackSide().toLowerCase().contains(filterPattern)) {
                        Log.d("Add card to Filter", "Card contains filter pattern, adding to filtered list");
                        filteredList.add(card);
                    }
                }
            }
            Log.d("Filtered List", "Size: " + filteredList.size());
            FilterResults results = new FilterResults();
            results.values = filteredList;
            
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cards.clear();
            cards.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
