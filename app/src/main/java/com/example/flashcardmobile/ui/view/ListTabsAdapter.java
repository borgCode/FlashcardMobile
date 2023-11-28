package com.example.flashcardmobile.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.fragment.CardListViewFragment;
import com.example.flashcardmobile.ui.fragment.DeckListViewFragment;
import com.example.flashcardmobile.ui.fragment.TagListViewFragment;

public class ListTabsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.list_tab_text_1, R.string.list_tab_text_2, R.string.list_tab_text_3};
    private final Context mContext;

    public ListTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new DeckListViewFragment();
            case 1:
                return new CardListViewFragment();
        }
        return new TagListViewFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
