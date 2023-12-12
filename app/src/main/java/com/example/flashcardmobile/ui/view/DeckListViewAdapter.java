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
import com.example.flashcardmobile.entity.DeckWithInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeckListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface OnButtonOperationListener {
        void onEditDeckName(long deckId, String deckName);

        void onViewAllCards(long deckId);

        void onViewDueCards(long deckId);

        void onResetDueDate(long id);

        void onCardDelete(long id);
    }

    private static final int VIEW_TYPE_DECKS = 0;
    private static final int VIEW_TYPE_ALL_CARDS = 1;
    private static final int VIEW_TYPE_DUE_CARDS = 2;

    private List<DeckWithInfo> decks;
    private List<Card> cards;
    private final OnButtonOperationListener listener;
    private boolean showingDecks = true;
    private boolean showingAllCards = false;

    public DeckListViewAdapter(OnButtonOperationListener listener) {
        decks = new ArrayList<>();
        this.listener = listener;

    }

    @Override
    public int getItemViewType(int position) {
        if (showingDecks) {
            
            return VIEW_TYPE_DECKS;
        } else if (showingAllCards) {
            
            return VIEW_TYPE_ALL_CARDS;
        } else {
            
            return VIEW_TYPE_DUE_CARDS;
        }
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {

        public TextView deckNameCol;
        public TextView deckSizeCol;
        public TextView dueCardSize;
        public ImageButton deckOptions;

        public DeckViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            deckNameCol = itemView.findViewById(R.id.deck_name_col);
            deckSizeCol = itemView.findViewById(R.id.deck_size_col);
            dueCardSize = itemView.findViewById(R.id.due_card_col);
            deckOptions = itemView.findViewById(R.id.list_deck_popup_menu);
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView frontSideCol;
        public TextView backSideCol;
        public ImageButton cardOptions;

        public CardViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            frontSideCol = itemView.findViewById(R.id.frontCol);
            backSideCol = itemView.findViewById(R.id.backCol);
            cardOptions = itemView.findViewById(R.id.list_card_popup_menu);
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_DECKS) {
            
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_deck_table_list_cell_view, parent, false);
            return new DeckViewHolder(view);
        } else {
            
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tag_table_card_cell_view, parent, false);
            return new CardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DeckViewHolder) {
            DeckViewHolder deckHolder = (DeckViewHolder) holder;
            DeckWithInfo deck = decks.get(position);

            deckHolder.deckNameCol.setText(deck.getDeck().getDeckName());
            deckHolder.deckSizeCol.setText(String.valueOf(deck.getDeckSize()));
            deckHolder.dueCardSize.setText(String.valueOf(deck.getDueCardSize()));

            deckHolder.deckOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(deckHolder.itemView.getContext(), deckHolder.deckOptions);
                popupMenu.getMenuInflater().inflate(R.menu.deck_list_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.edit_deck_name_item) {
                        listener.onEditDeckName(deck.getDeck().getId(), deck.getDeck().getDeckName());
                    } else if (id == R.id.view_all_cards_item) {
                        listener.onViewAllCards(deck.getDeck().getId());
                    } else if (id == R.id.view_due_cards_item) {
                        listener.onViewDueCards(deck.getDeck().getId());
                    } else {
                        return false;
                    }
                    return true;
                });

                popupMenu.show();
            });
        } else if (holder instanceof CardViewHolder) {
            CardViewHolder cardHolder = (CardViewHolder) holder;
            Card card = cards.get(position);
            
            cardHolder.frontSideCol.setText(card.getFrontSide());
            cardHolder.backSideCol.setText(card.getBackSide());

            cardHolder.cardOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), cardHolder.cardOptions);
                popupMenu.getMenuInflater().inflate(R.menu.card_deck_list_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.resetDueDateItem) {
                        listener.onResetDueDate(card.getId());
                    } else if (id == R.id.deleteCardItem) {
                        listener.onCardDelete(card.getId());
                    } else {
                        return false;
                    }
                    return true;
                });
                
                popupMenu.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        if (showingDecks) {
            return decks.size();
        } else {
            return cards.size();
        }
    }

    public void setDecks(ArrayList<DeckWithInfo> decks) {
        this.decks = decks;
        showingDecks = true;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
        showingDecks = false;
        showingAllCards = true;
    }

    public void setDueCards(ArrayList<Card> cards) {
        this.cards = cards;
        showingDecks = false;
        showingAllCards = false;
    }

    public void setShowingDecks() {
        showingDecks = true;
        showingAllCards = false;
    }


}
