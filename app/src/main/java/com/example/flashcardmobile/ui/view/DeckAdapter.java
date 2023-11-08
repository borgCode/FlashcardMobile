package com.example.flashcardmobile.ui.view;

import android.content.Intent;
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
import com.example.flashcardmobile.ui.activity.DeckActivity;
import com.example.flashcardmobile.entity.Deck;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<Deck> decks;

    public DeckAdapter(List<Deck> decks) {
        this.decks = decks;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_row, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeckViewHolder holder, int position) {

        Deck deck = decks.get(position);
        holder.deckBtn.setText(deck.getDeckName());

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

                        } else if (id == R.id.viewDeckItem) {
                            Intent intent = new Intent(view.getContext(), DeckActivity.class);
                            intent.putExtra("deckName", deck.getDeckName());
                            view.getContext().startActivity(intent);

                        } else if (id == R.id.deleteDeckItem) {

                        } else {
                            return false;
                        }
                        return true;
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return decks.size();
    }
}
