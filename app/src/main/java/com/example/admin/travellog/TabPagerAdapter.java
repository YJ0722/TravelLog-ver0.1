package com.example.admin.travellog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.admin.travellog.fragments.ExpenseFragment;
import com.example.admin.travellog.fragments.LogListFragment;
import com.example.admin.travellog.fragments.MemoFragment;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                MemoFragment tabFragment1 = new MemoFragment();
                return tabFragment1;
            case 1:
                ExpenseFragment tabFragment2 = new ExpenseFragment();
                return tabFragment2;
            case 2:
                LogListFragment tabFragment3 = new LogListFragment();
                return tabFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
