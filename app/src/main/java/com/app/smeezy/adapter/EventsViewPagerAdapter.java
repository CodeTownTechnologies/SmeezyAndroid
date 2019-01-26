package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.R;
import com.app.smeezy.fragment.BirthdayFragment;
import com.app.smeezy.fragment.DiscoverEventFragment;
import com.app.smeezy.fragment.PastEventFragment;
import com.app.smeezy.fragment.UpcomingEventFragment;

/**
 * Created by Rahul on 15-01-2018.
 */

public class EventsViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Context mContext;

    public EventsViewPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
        this.titles = mContext.getResources().getStringArray(R.array.event_tab_titles);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;


        switch (position) {

            case 0:

                fragment = new UpcomingEventFragment();

                break;

            case 1:

                fragment = new PastEventFragment();

                break;

            case 2:

                fragment = new DiscoverEventFragment();

                break;

            case 3:

                fragment = new BirthdayFragment();

                break;

        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
