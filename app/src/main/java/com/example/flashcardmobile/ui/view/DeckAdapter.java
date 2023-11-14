package com.example.flashcardmobile.ui.view;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Deck;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {

    public interface OnDeckOperationListener {
        void onDeleteDeck(long deckId);
        void onPracticeDeck(long deckId, String deckName);
        void onAddCard(long deckId);   
        void onViewDeck(long deckId, String deckName);
    }
    
    private List<Deck> decks;
    private OnDeckOperationListener listener;

    public DeckAdapter(OnDeckOperationListener listener, List<Deck> decks) {

        this.listener = listener;
        this.decks = new ArrayList<>();
    }

    public void setDecks(List<Deck> decks) {
        this.decks.clear();
        this.decks.addAll(decks);
        notifyDataSetChanged();
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        public Button deckBtn;
        private ImageButton deckOptions;

        public DeckViewHolder(View itemView) {
            super(itemView);
            deckBtn = itemView.findViewById(R.id.deckButton);
            deckOptions = itemView.findViewById(R.id.deckPopupMenu);
        }
    }


    @NonNull
    @NotNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_deck_selection_row, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeckViewHolder holder, int position) {

        Deck deck = decks.get(position);
        holder.deckBtn.setText(deck.getDeckName());
        holder.deckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPracticeDeck(deck.getId(), deck.getDeckName());
            }
        });

        holder.deckOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.deckOptions);
                popupMenu.getMenuInflater().inflate(R.menu.deck_popup_menu, popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.addCardItem) {
                            listener.onAddCard(deck.getId());

                        } else if (id == R.id.viewDeckItem) {
                            listener.onViewDeck(deck.getId(), deck.getDeckName());
                            
                        } else if (id == R.id.deleteDeckItem) {
                            listener.onDeleteDeck(deck.getId());
                            
                        } else {    
                            return false;
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        if (decks == null) {
            return 0;
        }
        return decks.size();
    }
}
