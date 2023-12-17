package com.example.flashcardmobile.ui.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.DeckCard;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardListViewAdapter extends RecyclerView.Adapter<CardListViewAdapter.ViewHolder> implements Filterable {

    public interface onCardOperationListener {
        void onCardEdit(long deckId, long cardId);

        void onResetDueDate(long cardId);

        void onCardDelete(long cardId);

    }

    public List<DeckCard> cards;
    private List<DeckCard> cardsFull;
    private final onCardOperationListener listener;
    private String currentSearchColumn;
    private SelectionTracker<Long> selectionTracker;
    private boolean isSelectionModeEnabled = false;
    private final Map<Long, Integer> idToPositionMap = new HashMap<>();


    public CardListViewAdapter(onCardOperationListener listener) {
        this.listener = listener;
        cards = new ArrayList<>();
        cardsFull = new ArrayList<>();
        setHasStableIds(true);
    }

    public void setCards(ArrayList<DeckCard> cards) {
        this.cards = cards;
        updateIdToPositionMap();
    }

    public void setCardsFull(ArrayList<DeckCard> cards) {
        this.cardsFull = cards;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void setSelectionModeEnabled(boolean enabled) {
        isSelectionModeEnabled = enabled;
        notifyDataSetChanged();
    }

    private void updateIdToPositionMap() {
        idToPositionMap.clear();
        for (int i = 0; i < cards.size(); i++) {
            idToPositionMap.put(cards.get(i).getCardId(), i);
        }
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
    public CardListViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_table_list_cell_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardListViewAdapter.ViewHolder holder, int position) {
        DeckCard card = cards.get(position);

        holder.deckNameCol.setText(card.getDeckName());
        holder.frontSideCol.setText(card.getFrontSide());
        holder.backSideCol.setText(card.getBackSide());
        LocalDate date = card.getDueDate().toLocalDate();
        holder.dueDateCol.setText(date.toString());

        long itemId = getItemId(position);
        boolean isSelected = selectionTracker.isSelected(itemId);
        Log.d("CardListViewAdapter", "onBindViewHolder: Position - " + position + ", Selected - " + isSelected);
        if (isSelectionModeEnabled) {
            if (isSelected) {
                holder.itemView.setBackgroundResource(R.drawable.selected_item_background);
            } else {
                holder.itemView.setBackground(null);
            }
        } else {
            holder.itemView.setBackground(null);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectionTracker != null) {
                selectionTracker.select(itemId);
            }
        });


        holder.cardOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.cardOptions);
            popupMenu.getMenuInflater().inflate(R.menu.card_list_popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.editCardItem) {
                    listener.onCardEdit(card.getDeckId(), card.getCardId());
                } else if (id == R.id.resetDueDateItem) {
                    listener.onResetDueDate(card.getCardId());
                } else if (id == R.id.deleteCardItem) {
                    listener.onCardDelete(card.getCardId());
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
        return cards.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DeckCard> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cardsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DeckCard card : cardsFull) {
                    switch (currentSearchColumn) {
                        case "deck":
                            if (card.getDeckName().equalsIgnoreCase(filterPattern)) {
                                filteredList.add(card);
                                break;
                            }
                        case "front side":
                            if (card.getFrontSide().toLowerCase().contains(filterPattern)) {
                                filteredList.add(card);
                                break;
                            }
                        case "back side":
                            if (card.getBackSide().toLowerCase().contains(filterPattern)) {
                                filteredList.add(card);
                                break;
                            }
                        case "tag":
                            filteredList.addAll(cardsFull);
                            break;
                    }
                }
            }
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

    public void setCurrentSearchColumn(String selectedOption) {
        this.currentSearchColumn = selectedOption;
    }

    public int getPositionForKey(Long key) {
        Integer position = idToPositionMap.get(key);
        return position != null ? position : RecyclerView.NO_POSITION;
    }

    @Override
    public long getItemId(int position) {
        long id = cards.get(position).getCardId();
        Log.d("CardListViewAdapter", "getItemId: Position - " + position + ", ID - " + id);
        return id;
    }

}
