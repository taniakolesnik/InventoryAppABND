package com.example.android.inventoryappabnd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tetianakolesnik on 09/04/2018.
 */

public class SlideFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    public SlideFragmentPagerAdapter(FragmentManager fm, Context appcontext) {
        super(fm);
        context = appcontext;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ItemsListFragment();
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                args.putBoolean(context.getString(R.string.in_stock), true);
                break;
            case 1:
                args.putBoolean(context.getString(R.string.in_stock), false);
                break;
            default:
                return null;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabName = "";
        switch (position) {
            case 0:
                tabName = context.getString(R.string.tab_name_available);
                break;
            case 1:
                tabName = context.getString(R.string.tab_name_soldout);;
                break;
        }
        return tabName;
    }
}