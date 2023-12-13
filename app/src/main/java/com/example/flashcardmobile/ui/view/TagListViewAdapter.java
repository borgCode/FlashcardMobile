package com.example.flashcardmobile.ui.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import com.example.flashcardmobile.entity.TagWithCardCount;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TagListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onTagOperationListener {
        void onViewCards(long tagId);

        void onEditTag(Tag tag);

        void onDeleteTag(Tag tag);

        void onChangeTags(long id);

        void onResetDueDate(long cardId);

        void onDeleteCard(Card card);
    }

    private static final int VIEW_TYPE_TAG = 0;
    private static final int VIEW_TYPE_CARD = 1;

    private List<TagWithCardCount> tags = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private final onTagOperationListener listener;
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
            TagWithCardCount tag = tags.get(position);
            tagHolder.tagName.setText(tag.getTag().getTagName());

            Drawable background = tagHolder.tagColor.getBackground();
            int rgbColor = tag.getTag().getColor();
            int argbColor = Color.argb(255, Color.red(rgbColor), Color.green(rgbColor), Color.blue(rgbColor));
            ((GradientDrawable) background).setColor(argbColor);

            tagHolder.tagSize.setText(String.valueOf(tag.getCardCount()));

            tagHolder.tagOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(tagHolder.itemView.getContext(), tagHolder.tagOptions);
                popupMenu.getMenuInflater().inflate(R.menu.tag_list_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.view_cards_item) {
                        listener.onViewCards(tag.getTag().getId());
                    } else if (id == R.id.edit_tag_item) {
                        listener.onEditTag(tag.getTag());
                    } else if (id == R.id.delete_tag_item) {
                        listener.onDeleteTag(tag.getTag());
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
                PopupMenu popupMenu = new PopupMenu(cardHolder.itemView.getContext(), cardHolder.cardOptions);
                popupMenu.getMenuInflater().inflate(R.menu.tag_card_list_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.change_tags_item) {
                        listener.onChangeTags(card.getId());
                    } else if (id == R.id.reset_due_date_item) {
                        listener.onResetDueDate(card.getId());
                    } else if (id == R.id.delete_card_item) {
                        listener.onDeleteCard(card);
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
        return showingTags ? tags.size() : cards.size();
    }

    public void setTags(List<TagWithCardCount> tags) {
        this.tags = tags;
        showingTags = true;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        showingTags = false;
    }

    public void setShowingTags(boolean isShowing) {
        showingTags = isShowing;
    }
}
