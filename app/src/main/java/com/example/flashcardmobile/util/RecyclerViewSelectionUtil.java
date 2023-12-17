package com.example.flashcardmobile.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class RecyclerViewSelectionUtil {

    public static <T> SelectionTracker<T> createSelectionTracker(
            String trackerId,
            RecyclerView recyclerView,
            ItemKeyProvider<T> keyProvider,
            ItemDetailsLookup<T> detailsLookup,
            StorageStrategy<T> storageStrategy) {

        return new SelectionTracker.Builder<>(
                trackerId,
                recyclerView,
                keyProvider,
                detailsLookup,
                storageStrategy
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build();
    }


    public static class KeyProvider<T> extends ItemKeyProvider<Long> {
        private final Function<Integer, Long> idGetter;

        public KeyProvider(int scope, Function<Integer, Long> idGetter) {
            super(scope);
            this.idGetter = idGetter;
            
        }

        @Override
        public Long getKey(int position) {
            Long id = idGetter.apply(position);
            Log.d("KeyProvider", "Position: " + position + ", ID: " +  id);
            return idGetter.apply(position);
        }

        
        @Override
        public int getPosition(@NonNull Long key) {
            return key.intValue();
        }
    }

    public static class DetailsLookup<T> extends ItemDetailsLookup<Long> {
        private final RecyclerView recyclerView;

        public DetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull @NotNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                long itemId = viewHolder.getItemId();
                Log.d("getItemDetails", " ID: " +  itemId);
                return new MyItemDetails(viewHolder.getBindingAdapterPosition(), itemId);
            }
            return null;
        }
    }

    private static class MyItemDetails extends ItemDetailsLookup.ItemDetails<Long> {
        private final int position;
        private final long itemId;

        public MyItemDetails(int position, long itemId) {
            this.position = position;
            this.itemId = itemId;
            Log.d("MyItemDetails", "Position: " + position + ", ID: " +  itemId);
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Nullable
        @Override
        public Long getSelectionKey() {
            return itemId;
        }
    }
}
