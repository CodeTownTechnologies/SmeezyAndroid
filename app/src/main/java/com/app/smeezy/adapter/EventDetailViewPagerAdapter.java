package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.R;
import com.app.smeezy.fragment.EventAboutFragment;
import com.app.smeezy.fragment.EventTimelineFragment;
import com.app.smeezy.responsemodels.Event;

/**
 * Created by Rahul on 15-01-2018.
 */

public class EventDetailViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Context mContext;
    private Event event;

    public EventDetailViewPagerAdapter(Context context, FragmentManager fragmentManager, Event event) {
        super(fragmentManager);
        this.mContext = context;
        this.titles = mContext.getResources().getStringArray(R.array.event_detail_tab_titles);
        this.event = event;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;


        switch (position) {

            case 0:

                fragment = new EventAboutFragment();

                break;

            /*case 1:

                fragment = new EventTimelineFragment();

                break;*/

        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("event", event);
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
