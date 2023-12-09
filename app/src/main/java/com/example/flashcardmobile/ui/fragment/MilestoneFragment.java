package com.example.flashcardmobile.ui.fragment;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.entity.Badge;
import com.example.flashcardmobile.viewmodel.BadgeViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MilestoneFragment extends Fragment {

    private BadgeViewModel badgeViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_milestones, container, false);

        List<ImageView> badgeImages = Arrays.asList(
                view.findViewById(R.id.cards_studied_1),
                view.findViewById(R.id.cards_studied_2),
                view.findViewById(R.id.cards_studied_3),
                view.findViewById(R.id.cards_mastered_1),
                view.findViewById(R.id.cards_mastered_2),
                view.findViewById(R.id.cards_mastered_3),
                view.findViewById(R.id.cards_added_1),
                view.findViewById(R.id.cards_added_2),
                view.findViewById(R.id.cards_added_3),
                view.findViewById(R.id.unique_days_1),
                view.findViewById(R.id.unique_days_2),
                view.findViewById(R.id.unique_days_3)
        );
        
        

        badgeViewModel = new ViewModelProvider(requireActivity()).get(BadgeViewModel.class);

        badgeViewModel.getAllBadges(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), badges -> {
            Log.d("Badge fetch", "Fetching badges, size of badges is: " + badges.size());
            if (badges.size() >= badgeImages.size()) {
                for (int i = 0; i < badgeImages.size(); i++) {
                    Log.d("Setting Badge Appearance", "Badge image: " + badgeImages.get(i) +
                            "\n badge: " + badges.get(i).getName());
                    setBadgeAppearance(badgeImages.get(i), badges.get(i));
                }
            }
        });


        return view;
    }

    private void setBadgeAppearance(ImageView badgeImage, Badge badge) {
        int drawableId = getDrawableId(badge);
        Drawable badgeDrawable = ContextCompat.getDrawable(getContext(), drawableId);
        
        if (badge.isAchieved()) {
            badgeDrawable.setAlpha(255);
            int borderId = getBorderDrawableId(badge.getLevel());
            Drawable borderDrawable = ContextCompat.getDrawable(getContext(), borderId);
            
            Drawable[] layers = new Drawable[2];
            layers[0] = borderDrawable;
            layers[1] = badgeDrawable;
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            
            badgeImage.setImageDrawable(layerDrawable);
        } else {
            badgeDrawable.setAlpha(50);
            badgeImage.setImageDrawable(badgeDrawable);
        }
    }

    private int getDrawableId(Badge badge) {
        switch (badge.getTag()) {
            case "study":
                return R.drawable.bell;
            case "master":
                return R.drawable.book;
            case "add":
                return R.drawable.key;
            case "days":
                return R.drawable.label;
            default:
                throw new IllegalArgumentException("Unknown tag: " + badge.getTag());
        }
    }
    private int getBorderDrawableId(int level) {
        switch (level) {
            case 1:
                return R.drawable.border_bronze;
            case 2:
                return R.drawable.border_silver;
            case 3:
                return R.drawable.border_gold;
            default:
                throw new IllegalArgumentException("Unknown badge level: " + level);
        }
    }
}
