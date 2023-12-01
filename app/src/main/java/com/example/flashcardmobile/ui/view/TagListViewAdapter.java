package com.example.flashcardmobile.ui.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import com.example.flashcardmobile.entity.Tag;
import com.example.flashcardmobile.entity.TagWithCards;
import org.jetbrains.annotations.NotNull;
import yuku.ambilwarna.AmbilWarnaDialog;

import java.util.ArrayList;
import java.util.List;

public class TagListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    public interface onTagOperationListener {
        void onViewCards(long tagId);
        
    }

    private static final int VIEW_TYPE_TAG = 0;
    private static final int VIEW_TYPE_CARD = 1;

    private List<Tag> tags = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private onTagOperationListener listener;
    private boolean showingTags = true;

    public TagListViewAdapter(onTagOperationListener listener) {
        this.listener = listener;
        
    }

    @Override
    public int getItemViewType(int position) {
        return showingTags ? VIEW_TYPE_TAG : VIEW_TYPE_CARD;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;
        public View tagColor;
        public TextView tagSize;
        public ImageButton tagOptions;

        public TagViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tagName = itemView.findViewById(R.id.tagName);
            tagColor = itemView.findViewById(R.id.colorCircle);
            tagSize = itemView.findViewById(R.id.tagSize);
            tagOptions = itemView.findViewById(R.id.list_tag_popup_menu);
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

        if (viewType == VIEW_TYPE_TAG) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tag_table_tag_cell_view, parent, false);
            return new TagViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tag_table_card_cell_view, parent, false);
            return new CardViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TagViewHolder) {
            TagViewHolder tagHolder = (TagViewHolder) holder;
            Tag tag = tags.get(position);
            tagHolder.tagName.setText(tag.getTagName());

            Drawable background = tagHolder.tagColor.getBackground();
            int rgbColor = tag.getColor(); 
            int argbColor = Color.argb(255, Color.red(rgbColor), Color.green(rgbColor), Color.blue(rgbColor));
            ((GradientDrawable) background).setColor(argbColor);

            tagHolder.tagSize.setText("0");

            tagHolder.tagOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(tagHolder.itemView.getContext(), tagHolder.tagOptions);
                popupMenu.getMenuInflater().inflate(R.menu.tag_list_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.view_cards_item) {
                        listener.onViewCards(tag.getId());
                    } else if (id == R.id.change_color_item) {
                        
                    } else if (id == R.id.edit_tag_item) {

                    } else if (id == R.id.delete_tag_item) {

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
            
//            cardHolder.cardOptions.setOnClickListener(v -> {
//                
//            });
        }

    }
    

    @Override
    public int getItemCount() {
        return showingTags ? tags.size() : cards.size();
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
        showingTags = true;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        showingTags = false;
    }
}