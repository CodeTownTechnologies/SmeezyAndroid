package com.app.smeezy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.smeezy.R;
import com.app.smeezy.fragment.FriendListFragment;
import com.app.smeezy.fragment.MyEventFragment;
import com.app.smeezy.fragment.MyFeedbackFragment;
import com.app.smeezy.fragment.MyGroupFragment;
import com.app.smeezy.fragment.MyShareFragment;
import com.app.smeezy.fragment.MyStuffFragment;
import com.app.smeezy.fragment.MyWishlistFragment;
import com.app.smeezy.responsemodels.User;

/**
 * Created by Rahul on 15-01-2018.
 */

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Context mContext;
    private String memberId;
    private boolean ownProfile;
    private User user;


    public ProfileViewPagerAdapter(Context context, FragmentManager fragmentManager, String memberId,
                                   boolean ownProfile, User user) {
        super(fragmentManager);
        this.mContext = context;
        this.memberId = memberId;
        this.ownProfile = ownProfile;
        this.user = user;
        if (ownProfile) {
            this.titles = mContext.getResources().getStringArray(R.array.own_profile_tab_titles);
        } else {
            this.titles = mContext.getResources().getStringArray(R.array.profile_tab_titles);
        }

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;


        switch (position) {

            case 0:

                fragment = new MyShareFragment();

                break;

            case 1:

                fragment = new FriendListFragment();

                break;

            case 2:

                fragment = new MyStuffFragment();


                break;

            case 3:

                fragment = new MyWishlistFragment();


                break;

            case 4:

                fragment = new MyGroupFragment();


                break;

            case 5:

                fragment = new MyEventFragment();

                break;

            case 6:

                fragment = new MyFeedbackFragment();

                break;


        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("memberId", memberId);
            bundle.putBoolean("ownProfile", ownProfile);
            bundle.putSerializable("user", user);
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
