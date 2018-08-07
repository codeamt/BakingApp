package com.example.annmargaret.bakingapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.annmargaret.bakingapp.ui.Tab1;
import com.example.annmargaret.bakingapp.ui.Tab2;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumbOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNumbOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab1 tabOne = new Tab1();
                return tabOne;

            case 1:
                Tab2 tabTwo = new Tab2();
                return tabTwo;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}
