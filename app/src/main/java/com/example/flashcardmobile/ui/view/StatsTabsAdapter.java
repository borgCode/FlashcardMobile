package com.example.flashcardmobile.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.flashcardmobile.R;
import com.example.flashcardmobile.ui.fragment.MilestoneFragment;
import com.example.flashcardmobile.ui.fragment.PerformanceFragment;
import com.example.flashcardmobile.ui.fragment.ProgressFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class StatsTabsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.stats_tab_text_1, R.string.stats_tab_text_2, R.string.stats_tab_text_3};
    private final Context mContext;

    public StatsTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProgressFragment();
            case 1:
                return new PerformanceFragment();
        }
        return new MilestoneFragment();
    }

        @Nullable
        @Override
        public CharSequence getPageTitle ( int position){
            return mContext.getResources().getString(TAB_TITLES[position]);
        }

        @Override
        public int getCount () {
            return 3;
        }
    }