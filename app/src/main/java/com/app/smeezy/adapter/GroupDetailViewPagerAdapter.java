package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.R;
import com.app.smeezy.fragment.GroupAboutFragment;
import com.app.smeezy.fragment.GroupTimelineFragment;
import com.app.smeezy.responsemodels.Group;

/**
 * Created by Rahul on 15-01-2018.
 */

public class GroupDetailViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Context mContext;
    private Group group;

    public GroupDetailViewPagerAdapter(Context context, FragmentManager fragmentManager, Group group) {
        super(fragmentManager);
        this.mContext = context;
        this.titles = mContext.getResources().getStringArray(R.array.group_detail_tab_titles);
        this.group = group;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;


        switch (position) {

           /* case 0:

                fragment = new GroupAboutFragment();

                break;*/

            case 0:

                fragment = new GroupTimelineFragment();

                break;

        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", group);
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
